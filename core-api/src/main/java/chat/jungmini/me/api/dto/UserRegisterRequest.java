package chat.jungmini.me.api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import chat.jungmini.me.model.User;

public record UserRegisterRequest(
        @NotBlank(message = "이메일은 필수입니다") @Email(message = "유효한 이메일 형식이어야 합니다") String email,
        @NotBlank(message = "닉네임은 필수입니다") @Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다") String nickname,
        @NotBlank(message = "비밀번호는 필수입니다") @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다") String password) {

    public User toDomain() {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
