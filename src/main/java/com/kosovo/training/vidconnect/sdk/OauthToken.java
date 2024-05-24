package com.kosovo.training.vidconnect.sdk;

public record OauthToken(String access_token, int expires_in, String id_token, String scope, String token_type) {
}
