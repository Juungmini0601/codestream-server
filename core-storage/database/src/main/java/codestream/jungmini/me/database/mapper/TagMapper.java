package codestream.jungmini.me.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import codestream.jungmini.me.model.ArticleTag;
import codestream.jungmini.me.model.Tag;

@Mapper
public interface TagMapper {
    void save(Tag tag);

    void saveAll(@Param("tags") List<Tag> tags);

    void saveArticleTags(@Param("articleTags") List<ArticleTag> articleTags);

    List<Tag> findByNames(@Param("names") List<String> names);
}
