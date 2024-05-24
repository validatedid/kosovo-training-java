package com.kosovo.training.vidconnect.api.controllers;

import com.kosovo.training.vidconnect.sdk.OpenIdConfig;
import com.kosovo.training.vidconnect.sdk.VIDconnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("vidconnect-back")
public class AuthorizationController {
    private final AppConfig config;

    @Autowired
    public AuthorizationController(AppConfig appConfig) {
        this.config = appConfig;
    }


    @PostMapping("authorize")
    public AuthorizationResponseDto processRequest(@RequestBody AuthorizationRequestDto request) throws ExecutionException, InterruptedException {
        var openIdConfig = new OpenIdConfig(config.getOpenid_url(), config.getOpenid_client_id(), config.getOpenid_client_secret());
        var vidconnect = new VIDconnect(openIdConfig);
        CompletableFuture<Object> credentialFuture = vidconnect.getCredential(request.code(), request.redirectUri());
        var credential = credentialFuture.get();
        return new AuthorizationResponseDto(credential);
    }
}
