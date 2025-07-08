package codestream.jungmini.me.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.repository.CategoryRepository;
import codestream.jungmini.me.model.Category;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;
import codestream.jungmini.me.support.response.CursorResponse;

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

    @Transactional(readOnly = true)
    public CursorResponse<Category, Long> getCategories(final Long cursor, final int size) {
        // 다음 데이터가 있는지 확인 하기 위해 + 1 해서 조회
        List<Category> categories = categoryRepository.findCategories(cursor, size + 1);
        // 다음 데이터가 있으면 size가 + 1 되어 있음, 응답 데이터는 페이징 사이즈 만큼 맞추기 위해서 하나 줄여줌
        boolean hasNext = categories.size() > size;

        if (hasNext) {
            categories.removeLast();
        }

        Long nextCursor = hasNext ? categories.getLast().getCategoryId() : null;

        return CursorResponse.of(categories, nextCursor, hasNext);
    }
}
