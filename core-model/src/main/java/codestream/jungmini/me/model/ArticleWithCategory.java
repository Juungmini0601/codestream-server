package codestream.jungmini.me.model;

import java.time.LocalDateTime;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = "articleId")
@NoArgsConstructor
@AllArgsConstructor
public class ArticleWithCategory {
    private Long articleId;
    private String title;
    private String author;
    private String description;
    private String thumbnailUrl;
    private String link;
    private Long categoryId;
    private String categoryName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
