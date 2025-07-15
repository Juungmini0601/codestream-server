package codestream.jungmini.me.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.mapper.ArticleMapper;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.ArticleWithDetails;

@Repository
@RequiredArgsConstructor
public class ArticleRepository {
    private final ArticleMapper articleMapper;

    @Transactional
    public void save(Article article) {
        articleMapper.save(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleWithDetails> findAllWithDetails(Long cursor, int size) {
        return articleMapper.findAllWithDetails(cursor, size);
    }

    @Transactional
    public void update(Article article) {
        articleMapper.update(article);
    }

    @Transactional
    public void deleteById(Long articleId) {
        articleMapper.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long articleId) {
        return articleMapper.existsById(articleId);
    }
}
