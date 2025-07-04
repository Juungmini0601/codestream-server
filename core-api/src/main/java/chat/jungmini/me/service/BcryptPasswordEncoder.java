package chat.jungmini.me.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
