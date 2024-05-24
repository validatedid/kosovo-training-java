package com.kosovo.training.vidconnect.sdk;

import java.util.Base64;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IdToken {
    private final String idToken;
    private final Object idTokenParsed;

    public IdToken(String idToken) {
        this.idToken = Objects.requireNonNull(idToken);
        this.idTokenParsed = parseJwt();
    }

    private Object parseJwt() {
        String[] tokenParts = idToken.split("\\.");
        String base64Payload = tokenParts[1];
        byte[] payloadBytes = Base64.getDecoder().decode(addPadding(base64Payload));
        String payloadJson = new String(payloadBytes);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(payloadJson, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JWT payload", e);
        }
    }

    private String addPadding(String base64String) {
        int missingPadding = 4 - (base64String.length() % 4);

        if (missingPadding != 4) {
            return base64String + "=".repeat(missingPadding);
        } else {
            return base64String;
        }
    }

    public Object getVerifiableCredential() {
        if (idTokenParsed instanceof java.util.Map) {
            java.util.Map<?, ?> idTokenParsedMap = (java.util.Map<?, ?>) idTokenParsed;
            Object vp = idTokenParsedMap.get("vp");
            if (vp instanceof java.util.Map) {
                java.util.Map<?, ?> vpMap = (java.util.Map<?, ?>) vp;
                Object verifiableCredential = vpMap.get("verifiableCredential");
                if (verifiableCredential instanceof java.util.List) {
                    java.util.List<?> verifiableCredentialList = (java.util.List<?>) verifiableCredential;
                    if (!verifiableCredentialList.isEmpty()) {
                        Object firstCredential = verifiableCredentialList.get(0);
                        if (firstCredential instanceof java.util.Map) {
                            java.util.Map<?, ?> firstCredentialMap = (java.util.Map<?, ?>) firstCredential;
                            Object payload = firstCredentialMap.get("payload");
                            if (payload instanceof java.util.Map) {
                                java.util.Map<?, ?> payloadMap = (java.util.Map<?, ?>) payload;
                                Object vc = payloadMap.get("vc");
                                if (vc != null) {
                                    return vc;
                                }
                            }
                        }
                        return firstCredential;
                    }
                }
            }
        }
        return false;
    }
}
