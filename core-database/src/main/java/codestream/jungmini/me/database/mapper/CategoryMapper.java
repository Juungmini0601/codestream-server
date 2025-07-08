package codestream.jungmini.me.database.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import codestream.jungmini.me.model.Category;

@Mapper
public interface CategoryMapper {
    boolean existsByName(final String name);

    Optional<Category> findByName(final String name);

    Optional<Category> findById(final Long id);

    void update(Category category);

    void deleteById(final Long id);

    void save(Category category);

    List<Category> findCategories(@Param("cursor") Long cursor, @Param("size") int size);
}
