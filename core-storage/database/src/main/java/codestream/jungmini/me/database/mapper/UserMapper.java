package codestream.jungmini.me.database.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import codestream.jungmini.me.model.OAuthProvider;
import codestream.jungmini.me.model.User;

@Mapper
public interface UserMapper {

    boolean existsByEmail(@Param("email") String email);

    User findById(Long id);

    Optional<User> findByEmail(@Param("email") String email);

    void save(User user);

    Optional<User> findByProviderIdAndProvider(String providerId, OAuthProvider provider);
}
