package com.kosovo.training.vidconnect.sdk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

public class VIDconnect {
    private final OpenIdConfig config;

    public VIDconnect(OpenIdConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    public CompletableFuture<Object> getCredential(String code, String redirectUri) {
        OpenIdApi vidconnectApi = new OpenIdApi(config);
        return vidconnectApi.getOauthTokenAsync(code, redirectUri)
                .thenApply(oauthToken -> new IdToken(oauthToken.id_token()).getVerifiableCredential());
    }
}

class OpenIdApi {
    private final OpenIdConfig config;
    private final HttpClient httpClient;

    public OpenIdApi(OpenIdConfig config) {
        this.config = Objects.requireNonNull(config);
        this.httpClient = HttpClient.newHttpClient();
    }

    public CompletableFuture<OauthToken> getOauthTokenAsync(String code, String redirectUri) {
        Map<String, String> body = getBody(code, redirectUri);
        String oauthTokenUrl = config.url() + "/oauth2/token";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(oauthTokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(buildFormDataFromMap(body))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    try {
                        return new ObjectMapper().readValue(responseBody, OauthToken.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing OAuth token", e);
                    }
                });
    }

    private Map<String, String> getBody(String code, String redirectUri) {
        Map<String, String> body = new HashMap<>();
        body.put("code", code);
        body.put("client_id", config.clientId());
        body.put("client_secret", config.clientSecret());
        body.put("redirect_uri", redirectUri);
        body.put("grant_type", "authorization_code");
        return body;
    }

    private HttpRequest.BodyPublisher buildFormDataFromMap(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
