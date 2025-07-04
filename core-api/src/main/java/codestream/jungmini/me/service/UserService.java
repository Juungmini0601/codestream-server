package codestream.jungmini.me.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.database.repository.UserRepository;
import codestream.jungmini.me.model.User;
import codestream.jungmini.me.support.error.ChatCustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ChatCustomException(
                    ErrorType.VALIDATION_ERROR, String.format("%s는 중복된 이메일 입니다", user.getEmail()));
        }

        String hashedPassword = passwordEncoder.hash(user.getPassword());
        user.changePassword(hashedPassword);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(final String email, final String password) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ChatCustomException(
                        ErrorType.VALIDATION_ERROR, String.format("%s는 존재하지 않는 이메일 입니다", email)));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ChatCustomException(ErrorType.AUTHENTICATION_ERROR, "이메일과 비밀번호를 다시 확인 해주세요");
        }

        return user;
    }
}
