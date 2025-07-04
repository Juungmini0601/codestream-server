package codestream.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.CheckVerificationCodeRequest;
import codestream.jungmini.me.api.dto.SendVerificationCodeRequest;
import codestream.jungmini.me.api.dto.UserLoginRequest;
import codestream.jungmini.me.model.UserSession;
import codestream.jungmini.me.service.AuthService;
import codestream.jungmini.me.support.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;

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
    public ApiResponse<UserSession> login(@Valid @RequestBody UserLoginRequest request) {
        UserSession userSession = authService.login(request.email(), request.password());

        return ApiResponse.success(userSession);
    }
}
