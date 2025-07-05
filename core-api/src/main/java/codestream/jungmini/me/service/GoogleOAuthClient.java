package codestream.jungmini.me.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.GoogleTokenResponse;
import codestream.jungmini.me.api.dto.GoogleUserResponse;
import codestream.jungmini.me.api.dto.OauthResponse;
import codestream.jungmini.me.model.OAuthProvider;
import codestream.jungmini.me.support.config.OAuthConfig;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthClient implements OauthClient {

    private final OAuthConfig.GoogleOAuthProperties googleOAuthProperties;
    private final RestClient restClient;

    @Override
    public OauthResponse getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        GoogleUserResponse googleUserResponse = restClient
                .get()
                .uri("https://www.googleapis.com/oauth2/v1/userinfo")
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new CustomException(ErrorType.AUTHENTICATION_ERROR, "구글 유저 정보 조회 실패");
                })
                .body(GoogleUserResponse.class);

        return new OauthResponse(googleUserResponse.id(), googleUserResponse.email(), OAuthProvider.GOOGLE);
    }

    private String getAccessToken(String code) {
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", googleOAuthProperties.getClientId());
        formData.add("client_secret", googleOAuthProperties.getClientSecret());
        formData.add("redirect_uri", googleOAuthProperties.getRedirectUri());
        formData.add("code", code);

        return restClient
                .post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new CustomException(ErrorType.AUTHENTICATION_ERROR, "구글 액세스 토큰 획득 실패");
                })
                .body(GoogleTokenResponse.class)
                .accessToken();
    }

    @Override
    public String loginPageUrl() {
        return new StringBuilder(googleOAuthProperties.getBaseUrl())
                .append("?response_type=code")
                .append("&client_id=")
                .append(googleOAuthProperties.getClientId())
                .append("&redirect_uri=")
                .append(googleOAuthProperties.getRedirectUri())
                .append("&scope=email")
                .toString();
    }

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.GOOGLE;
    }
}
