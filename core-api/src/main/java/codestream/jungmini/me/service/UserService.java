package codestream.jungmini.me.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.database.repository.UserRepository;
import codestream.jungmini.me.model.User;
import codestream.jungmini.me.model.UserRole;
import codestream.jungmini.me.support.error.CustomException;
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
            throw new CustomException(ErrorType.VALIDATION_ERROR, String.format("%s는 중복된 이메일 입니다", user.getEmail()));
        }

        if (user.getEmail().equals("jungmini0601@gmail.com")) {
            user.changeRole(UserRole.ROLE_ADMIN);
        }

        String hashedPassword = passwordEncoder.hash(user.getPassword());
        user.changePassword(hashedPassword);

        return userRepository.save(user);
    }
}
