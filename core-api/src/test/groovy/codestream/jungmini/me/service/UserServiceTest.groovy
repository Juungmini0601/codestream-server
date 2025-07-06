package codestream.jungmini.me.service

import spock.lang.Specification

import codestream.jungmini.me.database.repository.UserRepository
import codestream.jungmini.me.model.User
import codestream.jungmini.me.support.error.CustomException
import codestream.jungmini.me.support.error.ErrorType

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
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
}
