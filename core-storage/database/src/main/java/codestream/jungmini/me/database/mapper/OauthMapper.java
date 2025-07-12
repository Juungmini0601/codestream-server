package codestream.jungmini.me.database.mapper;

import org.apache.ibatis.annotations.Mapper;

import codestream.jungmini.me.model.Oauth;

@Mapper
public interface OauthMapper {
    void save(Oauth oauth);
}
