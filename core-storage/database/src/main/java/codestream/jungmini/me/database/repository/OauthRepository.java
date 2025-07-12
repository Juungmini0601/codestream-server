package codestream.jungmini.me.database.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.mapper.OauthMapper;
import codestream.jungmini.me.model.Oauth;

@Repository
@RequiredArgsConstructor
public class OauthRepository {

    private final OauthMapper oauthMapper;

    @Transactional
    public void save(Oauth oauth) {
        oauthMapper.save(oauth);
    }
}
