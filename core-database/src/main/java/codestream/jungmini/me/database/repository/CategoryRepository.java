package codestream.jungmini.me.database.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.mapper.CategoryMapper;
import codestream.jungmini.me.model.Category;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public boolean existsByName(final String name) {
        return categoryMapper.existsByName(name);
    }

    @Transactional
    public Category save(Category category) {
        categoryMapper.save(category);
        return category;
    }
}
