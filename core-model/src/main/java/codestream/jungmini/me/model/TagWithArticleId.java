package codestream.jungmini.me.model;

import java.time.LocalDateTime;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = "tagId")
@NoArgsConstructor
@AllArgsConstructor
public class TagWithArticleId {
    private Long articleId;
    private Long tagId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TagWithArticleId from(final String name) {
        return builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
