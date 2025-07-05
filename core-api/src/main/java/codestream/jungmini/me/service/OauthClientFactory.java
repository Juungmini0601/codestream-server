package codestream.jungmini.me.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import codestream.jungmini.me.model.OAuthProvider;

@Component
public class OauthClientFactory {
    private final Map<OAuthProvider, OauthClient> clientMap;

    public OauthClientFactory(List<OauthClient> clients) {
        clientMap = new HashMap<>();
        clients.forEach(client -> clientMap.put(client.getProvider(), client));
    }

    public OauthClient getClient(OAuthProvider provider) {
        return clientMap.get(provider);
    }
}
