package chat.jungmini.me.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import chat.jungmini.me.api.dto.UserLoginRequest;
import chat.jungmini.me.api.dto.UserRegisterRequest;
import chat.jungmini.me.model.User;
import chat.jungmini.me.model.UserSession;
import chat.jungmini.me.service.UserService;
import chat.jungmini.me.support.aop.Auth;
import chat.jungmini.me.support.constant.Constants;
import chat.jungmini.me.support.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping("/api/v1/users")
    public ApiResponse<?> register(@RequestBody @Valid UserRegisterRequest request) {
        User user = request.toDomain();
        userService.addUser(user);

        return ApiResponse.success();
    }

    @PostMapping("/api/v1/users/login")
    public ApiResponse<?> login(@RequestBody @Valid UserLoginRequest request, HttpServletRequest servletRequest) {
        User user = userService.login(request.email(), request.password());
        UserSession userSession = UserSession.from(user);
        servletRequest.getSession().setAttribute(Constants.USER_SESSION_KEY, userSession);
        return ApiResponse.success();
    }

    @GetMapping("/api/v1/users/me")
    public ApiResponse<UserSession> me(@Auth UserSession userSession) {
        return ApiResponse.success(userSession);
    }
}
