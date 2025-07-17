package codestream.jungmini.me.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.repository.ArticleRepository;
import codestream.jungmini.me.database.repository.TagRepository;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.ArticleWithDetails;
import codestream.jungmini.me.model.Tag;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;
import codestream.jungmini.me.support.response.CursorResponse;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public CursorResponse<ArticleWithDetails, Long> getArticlesWithDetails(Long cursor, int size) {
        // 다음 데이터가 있는지 확인하기 위해 + 1 해서 조회
        List<ArticleWithDetails> articles = articleRepository.findAllWithDetails(cursor, size + 1);
        // 다음 데이터가 있으면 size가 + 1 되어 있음, 응답 데이터는 페이징 사이즈 만큼 맞추기 위해서 하나 줄여줌
        boolean hasNext = articles.size() > size;

        if (hasNext) {
            articles.removeLast();
        }

        Long nextCursor = hasNext ? articles.getLast().getArticleId() : null;

        return CursorResponse.of(articles, nextCursor, hasNext);
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
