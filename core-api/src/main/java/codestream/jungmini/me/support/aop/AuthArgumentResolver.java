package codestream.jungmini.me.support.aop;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import codestream.jungmini.me.model.UserSession;
import codestream.jungmini.me.support.constant.Constants;
import codestream.jungmini.me.support.error.ChatCustomException;
import codestream.jungmini.me.support.error.ErrorType;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class)
                && parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        UserSession currentUser = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION_KEY);

        if (currentUser == null) {
            throw new ChatCustomException(ErrorType.AUTHENTICATION_ERROR, "인증이 필요한 요청입니다.");
        }

        return currentUser;
    }
}
