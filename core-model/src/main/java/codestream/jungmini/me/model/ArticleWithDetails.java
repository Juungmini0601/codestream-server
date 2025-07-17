package codestream.jungmini.me.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = "articleId")
@NoArgsConstructor
@AllArgsConstructor
public class ArticleWithDetails {
    private Long articleId;
    private String title;
    private String author;
    private String description;
    private String thumbnailUrl;
    private String link;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 리스트로 하면 쿼리에서 매핑이 안됨!
    private String tagNames;
    private String categoryName;

    public static ArticleWithDetails from(Article article, String tagNames, String categoryName) {
        return builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .author(article.getAuthor())
                .description(article.getDescription())
                .thumbnailUrl(article.getThumbnailUrl())
                .link(article.getLink())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .tagNames(tagNames)
                .categoryName(categoryName)
                .build();
    }
}
