package chat.jungmini.me.redis.service;

import java.time.Instant;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import chat.jungmini.me.model.UserSession;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository<? extends Session> httpSessionRepository;
    public static final String USER_SESSION_KEY = "USER_SESSION";

    public UserSession getUserSession(String httpSessionId) {
        Session session = httpSessionRepository.findById(httpSessionId);

        if (session != null) {
            return session.getAttribute(USER_SESSION_KEY);
        }

        return null;
    }

    public void refreshTTL(String httpSessionId) {
        Session session = httpSessionRepository.findById(httpSessionId);
        if (session != null) {
            session.setLastAccessedTime(Instant.now());
        }
    }
}
