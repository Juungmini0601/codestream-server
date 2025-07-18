package codestream.jungmini.me.database.mapper;

import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.ArticleWithDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ArticleMapper {
    void save(Article article);

    List<Article> findArticles(@Param("cursor") Long cursor, @Param("size") int size);

    Optional<ArticleWithDetails> findByIdWithDetail(@Param("articleId") Long articleId);

    void update(Article article);

    void deleteById(@Param("articleId") Long articleId);

    boolean existsById(@Param("articleId") Long articleId);

    void saveAll(List<Article> articles);
}
