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
@EqualsAndHashCode(of = {"articleId", "tagId"})
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag {
    private Long articleId;
    private Long tagId;
    private LocalDateTime createdAt;

    public static ArticleTag from(final Long articleId, final Long tagId) {
        return builder()
                .articleId(articleId)
                .tagId(tagId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
