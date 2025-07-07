package codestream.jungmini.me.database.mapper;

import org.apache.ibatis.annotations.Mapper;

import codestream.jungmini.me.model.Category;

@Mapper
public interface CategoryMapper {
    boolean existsByName(final String name);

    void save(Category category);
}
