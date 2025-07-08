package codestream.jungmini.me.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.CategoryResponse;
import codestream.jungmini.me.api.dto.CreateCategoryRequest;
import codestream.jungmini.me.api.dto.UpdateCategoryRequest;
import codestream.jungmini.me.model.Category;
import codestream.jungmini.me.service.CategoryService;
import codestream.jungmini.me.support.aop.Admin;
import codestream.jungmini.me.support.response.ApiResponse;
import codestream.jungmini.me.support.response.CursorResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @Admin
    @PostMapping("/api/v1/admin/categories")
    public ApiResponse<?> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        categoryService.createCategory(request.name());

        return ApiResponse.success();
    }

    @Admin
    @PatchMapping("/api/v1/admin/categories/{categoryId}")
    public ApiResponse<?> updateCategoryName(
            @PathVariable Long categoryId, @Valid @RequestBody UpdateCategoryRequest request) {
        categoryService.updateCategory(categoryId, request.name());

        return ApiResponse.success();
    }

    @Admin
    @DeleteMapping("/api/v1/admin/categories/{categoryId}")
    public ApiResponse<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return ApiResponse.success();
    }

    @GetMapping("/api/v1/categories")
    public ApiResponse<CursorResponse<CategoryResponse, Long>> getCategories(
            @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "10") int size) {
        CursorResponse<Category, Long> categoriesWithCursor = categoryService.getCategories(cursor, size);
        List<CategoryResponse> categoryResponses = categoriesWithCursor.getData().stream()
                .map(CategoryResponse::from)
                .toList();
        CursorResponse<CategoryResponse, Long> response = CursorResponse.of(
                categoryResponses, categoriesWithCursor.getNextCursor(), categoriesWithCursor.isHasNext());

        return ApiResponse.success(response);
    }
}
