package chat.jungmini.me.service

import spock.lang.Specification

import chat.jungmini.me.database.repository.UserRepository
import chat.jungmini.me.model.User
import chat.jungmini.me.support.error.ChatCustomException
import chat.jungmini.me.support.error.ErrorType

class UserServiceTest extends Specification {

    UserRepository userRepository = Stub()
    PasswordEncoder passwordEncoder = Stub()
    UserService userService = new UserService(userRepository, passwordEncoder)

    def "새로운 사용자를 성공적으로 등록한다"() {
        given:
        def user = User.builder()
                .email("test@example.com")
                .nickname("testuser")
                .password("plainPassword")
                .build()
        def hashedPassword = "hashedPassword123"
        
        userRepository.existsByEmail(user.getEmail()) >> false
        passwordEncoder.hash(user.getPassword()) >> hashedPassword
        userRepository.save(user) >> user

        when:
        def result = userService.addUser(user)

        then:
        result == user
        user.password == hashedPassword
    }

    def "중복된 이메일로 사용자 등록 시 예외를 발생시킨다"() {
        given:
        def user = User.builder()
                .email("duplicate@example.com")
                .nickname("testuser")
                .password("plainPassword")
                .build()
                
        userRepository.existsByEmail(user.getEmail()) >> true

        when:
        userService.addUser(user)

        then:
        def exception = thrown(ChatCustomException)
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
        def result = userService.login(email, password)

        then:
        result == user
    }

    def "존재하지 않는 이메일로 로그인 시 예외를 발생시킨다"() {
        given:
        def email = "notexist@example.com"
        def password = "plainPassword"
                
        userRepository.findByEmail(email) >> Optional.empty()

        when:
        userService.login(email, password)

        then:
        def exception = thrown(ChatCustomException)
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
        userService.login(email, password)

        then:
        def exception = thrown(ChatCustomException)
        exception.errorType == ErrorType.AUTHENTICATION_ERROR
    }


}
