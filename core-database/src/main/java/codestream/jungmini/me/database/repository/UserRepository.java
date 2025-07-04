package codestream.jungmini.me.database.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.mapper.UserMapper;
import codestream.jungmini.me.model.User;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public boolean existsByEmail(final String email) {
        return userMapper.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(final String email) {
        return userMapper.findByEmail(email);
    }

    @Transactional
    public User save(User user) {
        long id = userMapper.save(user);
        return User.builder()
                .userId(id)
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
