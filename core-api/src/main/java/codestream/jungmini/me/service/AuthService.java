package codestream.jungmini.me.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.repository.OauthRepository;
import codestream.jungmini.me.database.repository.UserRepository;
import codestream.jungmini.me.mail.service.MailService;
import codestream.jungmini.me.model.OAuthProvider;
import codestream.jungmini.me.model.Oauth;
import codestream.jungmini.me.model.User;
import codestream.jungmini.me.model.UserRole;
import codestream.jungmini.me.model.UserSession;
import codestream.jungmini.me.redis.service.RedisService;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationCodeGenerator verificationCodeGenerator;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final OauthRepository oauthRepository;

    private static final String VERIFICATION_CODE_PREFIX = "auth:verification-code:";
    private static final Integer VERIFICATION_TTL_SECOND = 5 * 60;
    private static final String VERIFICATION_EMAIL_PREFIX = "auth:verification:email:";

    public void sendVerificationCode(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, String.format("이미 존재하는 유저입니다. [%s]", email));
        }

        String verificationCode = verificationCodeGenerator.generate();
        redisService.set(VERIFICATION_CODE_PREFIX + email, verificationCode, VERIFICATION_TTL_SECOND);
        mailService.send(email, "[code stream] 인증 메일", String.format("회원 가입 창에 %s 를 입력해주세요", verificationCode));
    }

    public void checkVerificationCode(final String email, final String code) {
        String savedCode = redisService
                .get(VERIFICATION_CODE_PREFIX + email, String.class)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "인증 코드가 만료 되었습니다."));

        if (!savedCode.equals(code)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "인증 코드가 일치 하지 않습니다.");
        }

        redisService.delete(VERIFICATION_CODE_PREFIX + email);
        redisService.set(VERIFICATION_EMAIL_PREFIX + email, email, VERIFICATION_TTL_SECOND);
    }

    public boolean checkVerifiedEmail(final String email) {
        redisService
                .get(VERIFICATION_EMAIL_PREFIX + email, String.class)
                .orElseThrow(() -> new CustomException(ErrorType.VALIDATION_ERROR, "인증되지 않은 이메일 입니다."));

        return true;
    }

    public UserSession login(final String email, final String password) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new CustomException(ErrorType.VALIDATION_ERROR, String.format("이미 존재하는 유저입니다. [%s]", email)));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorType.AUTHENTICATION_ERROR, "이메일과 비밀번호를 다시 확인 해주세요");
        }

        return UserSession.from(user);
    }

    @Transactional
    public User oauth2Login(final String email, final String providerId, OAuthProvider provider) {
        // 소셜 로그인 정보가 있을 경우 유저 정보를 찾아서 반환
        return userRepository.findByProviderIdAndProvider(providerId, provider).orElseGet(() -> {
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                Oauth oauth = Oauth.with(user.getUserId(), provider, providerId);
                oauthRepository.save(oauth);
                return user;
            }

            // 회원을 생성
            UserRole role = email.equals("jungmini0601@gmail.com") ? UserRole.ROLE_ADMIN : UserRole.ROLE_USER;
            User newUser = User.from(email, passwordEncoder.hash(verificationCodeGenerator.generate()), role);
            User savedUser = userRepository.save(newUser);

            Oauth oauth = Oauth.with(savedUser.getUserId(), provider, providerId);
            oauthRepository.save(oauth);
            return savedUser;
        });
    }
}
