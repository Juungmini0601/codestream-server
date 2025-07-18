package codestream.jungmini.me.database.repository;

import codestream.jungmini.me.database.mapper.ArticleMapper;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.ArticleWithDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleRepository {
    private final ArticleMapper articleMapper;

    @Transactional
    public void save(Article article) {
        articleMapper.save(article);
    }

    @Transactional(readOnly = true)
    public List<Article> findArticles(Long cursor, int size) {
        return articleMapper.findArticles(cursor, size);
    }

    @Transactional(readOnly = true)
    public Optional<ArticleWithDetails> findByIdWithDetail(Long id) {
        return articleMapper.findByIdWithDetail(id);
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

    @Transactional
    public void saveAll(List<Article> articles) {
        articleMapper.saveAll(articles);
    }
}
