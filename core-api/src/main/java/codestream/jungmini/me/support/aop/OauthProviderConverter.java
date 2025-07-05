package codestream.jungmini.me.support.aop;

import org.springframework.core.convert.converter.Converter;

import codestream.jungmini.me.model.OAuthProvider;

public class OauthProviderConverter implements Converter<String, OAuthProvider> {
    @Override
    public OAuthProvider convert(String source) {
        return OAuthProvider.fromString(source);
    }
}
