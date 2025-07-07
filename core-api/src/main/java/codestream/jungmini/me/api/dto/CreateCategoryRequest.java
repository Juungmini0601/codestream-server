package codestream.jungmini.me.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message = "카테고리명은 필수입니다.") @Size(min = 2, max = 10, message = "카테고리명은 2자 이상 10자 이하여야 합니다.")
                String name) {}
