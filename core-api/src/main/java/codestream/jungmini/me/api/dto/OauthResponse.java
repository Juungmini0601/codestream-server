package codestream.jungmini.me.api.dto;

import codestream.jungmini.me.model.OAuthProvider;

public record OauthResponse(String providerId, String email, OAuthProvider provider) {}
