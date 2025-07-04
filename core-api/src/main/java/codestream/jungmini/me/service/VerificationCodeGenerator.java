package codestream.jungmini.me.service;

import org.springframework.stereotype.Component;

@Component
public class VerificationCodeGenerator {
    public String generate() {
        return String.format("%06d", (int) (Math.random() * 900000) + 100000);
    }
}
