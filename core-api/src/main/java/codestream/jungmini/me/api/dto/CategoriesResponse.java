package codestream.jungmini.me.api.dto;

import java.util.List;

import codestream.jungmini.me.model.Category;

public record CategoriesResponse(List<CategoryResponse> categories, Long nextCursor, boolean hasNext) {

    public static CategoriesResponse of(List<Category> categories, Long nextCursor, boolean hasNext) {
        List<CategoryResponse> categoryResponses =
                categories.stream().map(CategoryResponse::from).toList();

        return new CategoriesResponse(categoryResponses, nextCursor, hasNext);
    }
}
