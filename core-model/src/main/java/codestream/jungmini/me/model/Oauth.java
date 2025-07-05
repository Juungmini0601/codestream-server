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
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Oauth {
    private Long oauthId;
    private OAuthProvider provider;
    private String providerId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Oauth with(Long userId, OAuthProvider provider, String providerId) {
        return builder()
                .userId(userId)
                .provider(provider)
                .providerId(providerId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
