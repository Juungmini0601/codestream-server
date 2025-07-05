package codestream.jungmini.me.service;

import codestream.jungmini.me.api.dto.OauthResponse;
import codestream.jungmini.me.model.OAuthProvider;

public interface OauthClient {
    OauthResponse getUserInfo(String token);

    String loginPageUrl();

    OAuthProvider getProvider();
}
