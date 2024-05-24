package com.kosovo.training.vidconnect.api.controllers;

public record AuthorizationRequestDto(String code, String redirectUri) { }
