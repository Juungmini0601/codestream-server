package codestream.jungmini.me.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.repository.ArticleRepository;
import codestream.jungmini.me.database.repository.CategoryRepository;
import codestream.jungmini.me.database.repository.TagRepository;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.ArticleWithDetails;
import codestream.jungmini.me.model.Tag;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<ArticleWithDetails> getArticlesWithDetails(Long cursor, int size) {
        return articleRepository.findAllWithDetails(cursor, size);
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
