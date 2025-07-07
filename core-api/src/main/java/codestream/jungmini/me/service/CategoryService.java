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
}
