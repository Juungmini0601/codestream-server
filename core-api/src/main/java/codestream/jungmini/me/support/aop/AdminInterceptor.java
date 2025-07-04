package codestream.jungmini.me.support.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.model.UserRole;
import codestream.jungmini.me.model.UserSession;
import codestream.jungmini.me.support.constant.Constants;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            Admin adminAnnotation = handlerMethod.getMethodAnnotation(Admin.class);

            if (adminAnnotation != null) {
                UserSession currentUser = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
                if (currentUser == null || !currentUser.getRole().equals(UserRole.ROLE_ADMIN.toString())) {
                    throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "어드민 권한이 필요한 요청입니다.");
                }
            }
        }

        return true;
    }
}
