package codestream.jungmini.me.api.dto;

import java.util.Collections;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import codestream.jungmini.me.model.Article;

public record CreateArticleRequest(
        @NotBlank(message = "제목은 필수입니다") @Size(max = 255, message = "제목은 255자 이하여야 합니다") String title,
        @NotBlank(message = "작성자는 필수입니다") @Size(max = 100, message = "작성자는 100자 이하여야 합니다") String author,
        @NotBlank(message = "설명은 필수입니다") @Size(max = 1000, message = "설명은 1000자 이하여야 합니다") String description,
        @Size(max = 500, message = "썸네일 URL은 500자 이하여야 합니다") String thumbnailUrl,
        @NotBlank(message = "링크는 필수입니다") @Size(max = 500, message = "링크는 500자 이하여야 합니다") String link,
        @Size(max = 3, message = "태그는 최대 3개까지 가능합니다")
                Set<@NotBlank(message = "태그명은 필수입니다") @Size(max = 20, message = "태그명은 20자 이하여야 합니다") String> tags) {

    public Article toArticle() {
        return Article.from(title, author, description, thumbnailUrl, link);
    }

    public Set<String> getTagNames() {
        return tags != null ? tags : Collections.emptySet();
    }
}
