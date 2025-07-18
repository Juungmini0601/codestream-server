package codestream.jungmini.me.service;

import codestream.jungmini.me.database.repository.ArticleRepository;
import codestream.jungmini.me.database.repository.CategoryRepository;
import codestream.jungmini.me.database.repository.TagRepository;
import codestream.jungmini.me.model.*;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;
import codestream.jungmini.me.support.response.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public CursorResponse<ArticleWithTagCategory, Long> getArticlesWithTagCategory(Long cursor, int size) {
        List<Article> articles = articleRepository.findArticles(cursor, size + 1);

        List<Category> categories = categoryRepository.findAllByIds(
                articles.stream().map(Article::getCategoryId).toList());

        List<TagWithArticleId> tags = tagRepository.findTagByArticleIds(
                articles.stream().map(Article::getArticleId).toList());

        List<ArticleWithTagCategory> articleWithTagCategories = articles.stream()
                .map(article -> ArticleWithTagCategory.builder()
                        .article(article)
                        .tags(new ArrayList<>())
                        .build())
                .toList();

        // 각 게시글 마다 카테고리와 태그 리스트를 모두 돌아서 데이터를 맞춘다.
        for (ArticleWithTagCategory articleWithTagCategory : articleWithTagCategories) {
            Article article = articleWithTagCategory.getArticle();
            // 카테고리 데이터 조회
            for (Category category : categories) {
                if (article.getCategoryId().equals(category.getCategoryId())) {
                    articleWithTagCategory.setCategory(category);
                }
            }

            // 테그 데이터 조회
            for (TagWithArticleId tag : tags) {
                if (article.getArticleId().equals(tag.getArticleId())) {
                    articleWithTagCategory.getTags().add(tag);
                }
            }
        }

        boolean hasNext = articles.size() > size;

        if (hasNext) {
            articles.removeLast();
        }

        Long nextCursor = hasNext ? articles.getLast().getArticleId() : null;

        return CursorResponse.of(articleWithTagCategories, nextCursor, hasNext);
    }

    @Transactional(readOnly = true)
    public ArticleWithDetails getArticlesWithDetail(final Long id) {
        return articleRepository
                .findByIdWithDetail(id)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "존재하지 않는 게시글입니다."));
    }

    @Transactional
    public Article createArticle(Article article, Set<String> tagNames) {
        List<Tag> tags = tagRepository.findOrCreateTags(tagNames);

        articleRepository.save(article);
        tagRepository.saveArticleTags(article.getArticleId(), tags);

        return article;
    }

    @Transactional
    public Article updateArticle(Long articleId, Article article, Set<String> tagNames) {
        if (!articleRepository.existsById(articleId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, String.format("게시글을 찾을 수 없습니다: [%s]", articleId));
        }

        List<Tag> tags = tagRepository.findOrCreateTags(tagNames);

        articleRepository.update(article);

        tagRepository.deleteArticleTags(articleId);
        tagRepository.saveArticleTags(article.getArticleId(), tags);

        return article;
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, String.format("게시글을 찾을 수 없습니다: [%s]", articleId));
        }

        tagRepository.deleteArticleTags(articleId);
        articleRepository.deleteById(articleId);
    }
}
