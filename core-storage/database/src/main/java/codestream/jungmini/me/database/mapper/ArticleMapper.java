package codestream.jungmini.me.database.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.ArticleWithDetails;

@Mapper
public interface ArticleMapper {
    void save(Article article);

    List<ArticleWithDetails> findAllWithDetails(@Param("cursor") Long cursor, @Param("size") int size);

    void update(Article article);

    void deleteById(@Param("articleId") Long articleId);

    boolean existsById(@Param("articleId") Long articleId);
}
