package codestream.jungmini.me.database.mapper;

import org.apache.ibatis.annotations.Mapper;

import codestream.jungmini.me.model.Article;

@Mapper
public interface ArticleMapper {
    void save(Article article);
}
