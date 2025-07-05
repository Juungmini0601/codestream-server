package codestream.jungmini.me.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoUserResponse(String id, @JsonProperty("kakao_account") KakaoAccount kakaoAccount) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoAccount(String email) {}
}
