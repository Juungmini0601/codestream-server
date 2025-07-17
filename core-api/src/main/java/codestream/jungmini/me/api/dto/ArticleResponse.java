package codestream.jungmini.me.api.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import codestream.jungmini.me.model.ArticleWithDetails;

public record ArticleResponse(
        Long articleId,
        String title,
        String author,
        String description,
        String thumbnailUrl,
        String link,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> tagNames,
        String categoryName) {
    public static ArticleResponse from(ArticleWithDetails articleWithDetails) {
        List<String> tagNamesList = (articleWithDetails.getTagNames() == null
                        || articleWithDetails.getTagNames().isEmpty())
                ? Collections.emptyList()
                : Arrays.asList(articleWithDetails.getTagNames().split(","));

        return new ArticleResponse(
                articleWithDetails.getArticleId(),
                articleWithDetails.getTitle(),
                articleWithDetails.getAuthor(),
                articleWithDetails.getDescription(),
                articleWithDetails.getThumbnailUrl(),
                articleWithDetails.getLink(),
                articleWithDetails.getCreatedAt(),
                articleWithDetails.getUpdatedAt(),
                tagNamesList,
                articleWithDetails.getCategoryName());
    }
}
