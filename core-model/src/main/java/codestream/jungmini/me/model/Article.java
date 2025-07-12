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
public class Article {
    private Long articleId;
    private String title;
    private String author;
    private String description;
    private String thumbnailUrl;
    private String link;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Article from(final String title, final String author, final String description, 
                              final String thumbnailUrl, final String link) {
        return builder()
                .title(title)
                .author(author)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .link(link)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}