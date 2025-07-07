package codestream.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.CreateCategoryRequest;
import codestream.jungmini.me.api.dto.UpdateCategoryRequest;
import codestream.jungmini.me.service.CategoryService;
import codestream.jungmini.me.support.aop.Admin;
import codestream.jungmini.me.support.response.ApiResponse;

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
}
