package codestream.jungmini.me.service

import spock.lang.Specification

class VerificationCodeGeneratorTest extends Specification {
    VerificationCodeGenerator verificationCodeGenerator = new VerificationCodeGenerator()

    def "인증 번호는 6자리 숫자이다"() {
        when:
        def code = verificationCodeGenerator.generate()

        then:
        code.size() == 6
    }
}
