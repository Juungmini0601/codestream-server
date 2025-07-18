package codestream.jungmini.me.model;

import java.time.LocalDateTime;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = "articleId")
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long articleId;
    private String title;
    private String author;
    private String description;
    private String thumbnailUrl;
    private String link;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Article from(
            final String title,
            final String author,
            final String description,
            final String thumbnailUrl,
            final String link,
            final Long categoryId) {

        return builder()
                .title(title)
                .author(author)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .link(link)
                .categoryId(categoryId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
