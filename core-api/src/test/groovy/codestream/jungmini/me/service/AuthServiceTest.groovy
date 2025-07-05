package codestream.jungmini.me.service

import codestream.jungmini.me.database.repository.UserRepository
import codestream.jungmini.me.database.repository.OauthRepository
import codestream.jungmini.me.mail.service.MailService
import codestream.jungmini.me.redis.service.RedisService
import codestream.jungmini.me.model.User
import codestream.jungmini.me.model.Oauth
import codestream.jungmini.me.model.OAuthProvider
import codestream.jungmini.me.model.UserRole
import codestream.jungmini.me.support.error.CustomException
import codestream.jungmini.me.support.error.ErrorType
import spock.lang.Specification

class AuthServiceTest extends Specification {

    VerificationCodeGenerator verificationCodeGenerator = Stub()
    UserRepository userRepository = Mock()
    MailService mailService = Mock()
    RedisService redisService = Mock()
    PasswordEncoder passwordEncoder = Stub()
    OauthRepository oauthRepository = Mock()
    
    AuthService authService = new AuthService(
        verificationCodeGenerator,
        userRepository,
        mailService,
        redisService,
        passwordEncoder,
        oauthRepository
    )
    
    def "인증 코드 발송에 성공한다"() {
        given:
        def email = "test@example.com"
        def verificationCode = "123456"
        
        userRepository.existsByEmail(email) >> false
        verificationCodeGenerator.generate() >> verificationCode
        
        when:
        authService.sendVerificationCode(email)
        
        then:
        1 * redisService.set("auth:verification-code:" + email, verificationCode, 300)
        1 * mailService.send(email, "[code stream] 인증 메일", "회원 가입 창에 123456 를 입력해주세요")
    }
    
    def "이미 존재하는 이메일로 인증 코드 발송 시 예외를 발생시킨다"() {
        given:
        def email = "duplicate@example.com"
        
        userRepository.existsByEmail(email) >> true
        
        when:
        authService.sendVerificationCode(email)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
    
    def "인증 코드 검증에 성공한다"() {
        given:
        def email = "test@example.com"
        def code = "123456"
        
        redisService.get("auth:verification-code:" + email, String.class) >> Optional.of(code)
        
        when:
        authService.checkVerificationCode(email, code)
        
        then:
        1 * redisService.delete("auth:verification-code:" + email)
        1 * redisService.set("auth:verification:email:" + email, email, 300)
    }
    
    def "인증 코드가 만료된 경우 예외를 발생시킨다"() {
        given:
        def email = "test@example.com"
        def code = "123456"
        
        redisService.get("auth:verification-code:" + email, String.class) >> Optional.empty()
        
        when:
        authService.checkVerificationCode(email, code)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
    
    def "인증 코드가 일치하지 않는 경우 예외를 발생시킨다"() {
        given:
        def email = "test@example.com"
        def code = "123456"
        def savedCode = "654321"
        
        redisService.get("auth:verification-code:" + email, String.class) >> Optional.of(savedCode)
        
        when:
        authService.checkVerificationCode(email, code)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
    
    def "인증된 이메일 확인에 성공한다"() {
        given:
        def email = "test@example.com"
        
        redisService.get("auth:verification:email:" + email, String.class) >> Optional.of(email)
        
        when:
        def result = authService.checkVerifiedEmail(email)
        
        then:
        result == true
    }
    
    def "인증되지 않은 이메일 확인 시 예외를 발생시킨다"() {
        given:
        def email = "test@example.com"
        
        redisService.get("auth:verification:email:" + email, String.class) >> Optional.empty()
        
        when:
        authService.checkVerifiedEmail(email)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
    
    def "로그인에 성공한다"() {
        given:
        def email = "test@example.com"
        def password = "plainPassword"
        def hashedPassword = "hashedPassword123"
        def user = User.builder()
                .email(email)
                .nickname("testuser")
                .password(hashedPassword)
                .build()
        
        userRepository.findByEmail(email) >> Optional.of(user)
        passwordEncoder.matches(password, hashedPassword) >> true
        
        when:
        def result = authService.login(email, password)
        
        then:
        result.email == email
    }
    
    def "존재하지 않는 이메일로 로그인 시 예외를 발생시킨다"() {
        given:
        def email = "notexist@example.com"
        def password = "plainPassword"
        
        userRepository.findByEmail(email) >> Optional.empty()
        
        when:
        authService.login(email, password)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
    
    def "잘못된 비밀번호로 로그인 시 예외를 발생시킨다"() {
        given:
        def email = "test@example.com"
        def password = "wrongPassword"
        def hashedPassword = "hashedPassword123"
        def user = User.builder()
                .email(email)
                .nickname("testuser")
                .password(hashedPassword)
                .build()
        
        userRepository.findByEmail(email) >> Optional.of(user)
        passwordEncoder.matches(password, hashedPassword) >> false
        
        when:
        authService.login(email, password)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.AUTHENTICATION_ERROR
    }
    
    def "기존 소셜 로그인 정보가 있는 경우 유저를 반환한다"() {
        given:
        def email = "test@example.com"
        def providerId = "google123"
        def provider = OAuthProvider.GOOGLE
        def user = User.builder()
                .userId(1L)
                .email(email)
                .nickname("testuser")
                .build()
        
        userRepository.findByProviderIdAndProvider(providerId, provider) >> Optional.of(user)
        
        when:
        def result = authService.oauth2Login(email, providerId, provider)
        
        then:
        result == user
    }
    
    def "기존 일반 계정이 있는 경우 OAuth 정보를 추가한다"() {
        given:
        def email = "test@example.com"
        def providerId = "google123"
        def provider = OAuthProvider.GOOGLE
        def user = User.builder()
                .userId(1L)
                .email(email)
                .nickname("testuser")
                .build()
        
        userRepository.findByProviderIdAndProvider(providerId, provider) >> Optional.empty()
        userRepository.findByEmail(email) >> Optional.of(user)
        
        when:
        def result = authService.oauth2Login(email, providerId, provider)
        
        then:
        result == user
        1 * oauthRepository.save({ Oauth oauth ->
            oauth.userId == 1L &&
            oauth.provider == provider &&
            oauth.providerId == providerId
        })
    }
    
    def "새로운 사용자를 생성한다"() {
        given:
        def email = "test@example.com"
        def providerId = "google123"
        def provider = OAuthProvider.GOOGLE
        def hashedPassword = "hashedPassword123"
        def generatedCode = "randomCode"
        def newUser = User.builder()
                .userId(1L)
                .email(email)
                .password(hashedPassword)
                .role(UserRole.ROLE_USER)
                .build()
        
        userRepository.findByProviderIdAndProvider(providerId, provider) >> Optional.empty()
        userRepository.findByEmail(email) >> Optional.empty()
        verificationCodeGenerator.generate() >> generatedCode
        passwordEncoder.hash(generatedCode) >> hashedPassword
        userRepository.save(_) >> newUser
        
        when:
        def result = authService.oauth2Login(email, providerId, provider)
        
        then:
        result == newUser
        1 * oauthRepository.save({ Oauth oauth ->
            oauth.userId == 1L &&
            oauth.provider == provider &&
            oauth.providerId == providerId
        })
    }

}
