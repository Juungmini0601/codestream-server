package codestream.jungmini.me.service;

public interface PasswordEncoder {
    String hash(String password);

    boolean matches(String password, String hashed);
}
