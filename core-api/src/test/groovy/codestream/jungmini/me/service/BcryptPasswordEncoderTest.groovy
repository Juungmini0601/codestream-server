package codestream.jungmini.me.service

import spock.lang.Specification

class BcryptPasswordEncoderTest extends Specification {
    BcryptPasswordEncoder encoder = new BcryptPasswordEncoder()

    def "해시된 패스워드는 원본과 다르다"() {
        given:
        def password = "testPassword"

        when:
        def hashedPassword = encoder.hash(password)

        then:
        hashedPassword != password
    }

    def "올바른 패스워드는 해시와 매칭된다"() {
        given:
        def password = "testPassword"
        def hashedPassword = encoder.hash(password)

        when:
        def result = encoder.matches(password, hashedPassword)

        then:
        result
    }

    def "잘못된 패스워드가 해시와 매칭되지 않는다"() {
        given:
        def password = "testPassword"
        def wrongPassword = "wrongPassword"
        def hashedPassword = encoder.hash(password)

        when:
        def result = encoder.matches(wrongPassword, hashedPassword)

        then:
        !result
    }
}
