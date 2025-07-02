package chat.jungmini.me.model;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changePassword(final String password) {
        if (password == null) {
            throw new IllegalArgumentException("password is required value");
        }

        this.password = password;
    }
}
