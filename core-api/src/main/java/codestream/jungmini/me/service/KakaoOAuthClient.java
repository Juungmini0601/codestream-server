package codestream.jungmini.me.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.KakaoTokenResponse;
import codestream.jungmini.me.api.dto.KakaoUserResponse;
import codestream.jungmini.me.api.dto.OauthResponse;
import codestream.jungmini.me.model.OAuthProvider;
import codestream.jungmini.me.support.config.OAuthConfig;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuthClient implements OauthClient {

    private final OAuthConfig.KakaoOAuthProperties kakaoOAuthProperties;
    private final RestClient restClient;

    @Override
    public OauthResponse getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        KakaoUserResponse kakaoUserResponse = restClient
                .get()
                .uri(kakaoOAuthProperties.getResourceServerBaseUrl() + "/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException("Failed to load Kakao user info");
                })
                .body(KakaoUserResponse.class);

        return new OauthResponse(
                kakaoUserResponse.id(), kakaoUserResponse.kakaoAccount().email(), OAuthProvider.KAKAO);
    }

    private String getAccessToken(String code) {
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoOAuthProperties.getClientId());
        formData.add("redirect_uri", kakaoOAuthProperties.getRedirectUri());
        formData.add("code", code);

        log.info("{}", formData);

        return restClient
                .post()
                .uri(kakaoOAuthProperties.getAuthServerBaseUrl() + "/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new CustomException(ErrorType.AUTHENTICATION_ERROR, "카카오 유저 정보 조회 실패");
                })
                .body(KakaoTokenResponse.class)
                .accessToken();
    }

    @Override
    public String loginPageUrl() {
        return new StringBuilder(kakaoOAuthProperties.getAuthServerBaseUrl())
                .append("/oauth/authorize")
                .append("?response_type=code")
                .append("&client_id=")
                .append(kakaoOAuthProperties.getClientId())
                .append("&redirect_uri=")
                .append(kakaoOAuthProperties.getRedirectUri())
                .toString();
    }

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.KAKAO;
    }
}
