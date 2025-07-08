package codestream.jungmini.me.api.dto;

import java.time.LocalDateTime;

import codestream.jungmini.me.model.Category;

public record CategoryResponse(Long categoryId, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getCategoryId(), category.getName(), category.getCreatedAt(), category.getUpdatedAt());
    }
}
