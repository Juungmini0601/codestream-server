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
@EqualsAndHashCode(of = "userId")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private String email;
    private String nickname;
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changePassword(final String password) {
        if (password == null) {
            throw new IllegalArgumentException("password is required value");
        }

        this.password = password;
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }

    public static User from(final String email, final String password, UserRole role) {
        return builder()
                .email(email)
                .nickname("기본 닉네임")
                .password(password)
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
