package codestream.jungmini.me.model;

import lombok.*;

import java.util.List;

@Setter
@Builder
@Getter
@ToString
@EqualsAndHashCode(of = "articleId")
@NoArgsConstructor
@AllArgsConstructor
public class ArticleWithTagCategory {
    private Article article;
    private List<TagWithArticleId> tags;
    private Category category;
}
