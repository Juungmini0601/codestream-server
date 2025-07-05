package codestream.jungmini.me.api;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.CheckVerificationCodeRequest;
import codestream.jungmini.me.api.dto.OauthResponse;
import codestream.jungmini.me.api.dto.SendVerificationCodeRequest;
import codestream.jungmini.me.api.dto.UserLoginRequest;
import codestream.jungmini.me.model.OAuthProvider;
import codestream.jungmini.me.model.User;
import codestream.jungmini.me.model.UserSession;
import codestream.jungmini.me.service.AuthService;
import codestream.jungmini.me.service.OauthClient;
import codestream.jungmini.me.service.OauthClientFactory;
import codestream.jungmini.me.support.constant.Constants;
import codestream.jungmini.me.support.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;
    private final OauthClientFactory oauthClientFactory;

    @PostMapping("/api/v1/auth/verification-code")
    public ApiResponse<?> sendVerificationCode(@Valid @RequestBody SendVerificationCodeRequest request) {
        authService.sendVerificationCode(request.email());

        return ApiResponse.success();
    }

    @PostMapping("/api/v1/auth/verification-code/check")
    public ApiResponse<?> checkVerificationCode(@Valid @RequestBody CheckVerificationCodeRequest request) {
        authService.checkVerificationCode(request.email(), request.code());

        return ApiResponse.success();
    }

    @PostMapping("/api/v1/auth/login")
    public ApiResponse<UserSession> login(@Valid @RequestBody UserLoginRequest request, HttpSession session) {
        UserSession userSession = authService.login(request.email(), request.password());
        session.setAttribute(Constants.USER_SESSION_KEY, userSession);

        return ApiResponse.success(userSession);
    }

    @GetMapping("/api/v1/auth/{provider}/login")
    public void redirectOauthLoginPage(
            @PathVariable("provider") OAuthProvider provider, HttpServletResponse httpServletResponse)
            throws Exception {
        OauthClient client = oauthClientFactory.getClient(provider);
        String redirectUrl = client.loginPageUrl();

        httpServletResponse.sendRedirect(redirectUrl);
    }

    @GetMapping("/api/v1/auth/{provider}/login/callback")
    public ApiResponse<UserSession> oauthLoginCallback(
            @PathVariable("provider") OAuthProvider provider, @RequestParam String code, HttpSession session) {
        OauthClient client = oauthClientFactory.getClient(provider);
        OauthResponse oauthResponse = client.getUserInfo(code);

        User user =
                authService.oauth2Login(oauthResponse.email(), oauthResponse.providerId(), oauthResponse.provider());
        UserSession userSession = UserSession.from(user);

        session.setAttribute(Constants.USER_SESSION_KEY, userSession);
        return ApiResponse.success(userSession);
    }
}
