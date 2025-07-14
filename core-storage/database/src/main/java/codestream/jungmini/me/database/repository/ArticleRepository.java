package codestream.jungmini.me.database.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.mapper.ArticleMapper;
import codestream.jungmini.me.model.Article;

@Repository
@RequiredArgsConstructor
public class ArticleRepository {
    private final ArticleMapper articleMapper;

    @Transactional
    public void save(Article article) {
        articleMapper.save(article);
    }
}
