package codestream.jungmini.me.database.mapper;

import codestream.jungmini.me.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CategoryMapper {
    boolean existsByName(final String name);

    Optional<Category> findById(final Long id);

    List<Category> findAllByIds(List<Long> ids);

    void update(Category category);

    void deleteById(final Long id);

    void save(Category category);

    List<Category> findCategories(@Param("cursor") Long cursor, @Param("size") int size);
}
