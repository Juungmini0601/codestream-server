package codestream.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.UserRegisterRequest;
import codestream.jungmini.me.model.User;
import codestream.jungmini.me.model.UserSession;
import codestream.jungmini.me.service.AuthService;
import codestream.jungmini.me.service.UserService;
import codestream.jungmini.me.support.aop.Auth;
import codestream.jungmini.me.support.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/api/v1/users")
    public ApiResponse<?> register(@RequestBody @Valid UserRegisterRequest request) {
        User user = request.toDomain();
        authService.checkVerifiedEmail(user.getEmail());
        userService.addUser(user);

        return ApiResponse.success();
    }

    @GetMapping("/api/v1/users/me")
    public ApiResponse<UserSession> me(@Auth UserSession userSession) {
        return ApiResponse.success(userSession);
    }
}
