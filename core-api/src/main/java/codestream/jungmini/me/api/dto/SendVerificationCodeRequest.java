package codestream.jungmini.me.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SendVerificationCodeRequest(
        @Email(message = "이메일 형식으로 입력 해 주세요") @NotNull(message = "이메일은 비어 있을 수 없습니다.") String email) {}
