package codestream.jungmini.me.database.repository;

import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Optional<Category> findById(final Long id) {
        return categoryMapper.findById(id);
    }

    @Transactional
    public Category update(Category category) {
        categoryMapper.update(category);
        return category;
    }

    @Transactional
    public void deleteById(final Long id) {
        categoryMapper.deleteById(id);
    }

    @Transactional
    public Category save(Category category) {
        categoryMapper.save(category);
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> findCategories(final Long cursor, final int size) {
        return categoryMapper.findCategories(cursor, size);
    }
}
