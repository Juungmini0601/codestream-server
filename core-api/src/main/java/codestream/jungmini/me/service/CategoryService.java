package codestream.jungmini.me.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.repository.CategoryRepository;
import codestream.jungmini.me.model.Category;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(final String name) {
        if (categoryRepository.existsByName(name)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, String.format("[%s]는 중복된 카테고리 이름입니다.", name));
        }

        Category category = Category.from(name);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(final Long categoryId, final String name) {
        if (categoryRepository.existsByName(name)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, String.format("[%s]는 중복된 카테고리 이름입니다.", name));
        }

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "존재하지 않는 카테고리 입니다."));

        category.changeName(name);
        categoryRepository.update(category);

        return category;
    }

    @Transactional
    public Category deleteCategory(final Long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "존재하지 않는 카테고리 입니다."));

        categoryRepository.deleteById(category.getCategoryId());

        return category;
    }
}
