package com.kosovo.training.vidconnect.api.controllers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vidconnect")
public class AppConfig {
    private String openid_client_secret;

    private String openid_url;

    private String openid_client_id;

    public String getOpenid_client_secret() {
        return openid_client_secret;
    }

    public void setOpenid_client_secret(String openid_client_secret) {
        this.openid_client_secret = openid_client_secret;
    }

    public String getOpenid_url() {
        return openid_url;
    }

    public void setOpenid_url(String openid_url) {
        this.openid_url = openid_url;
    }

    public String getOpenid_client_id() {
        return openid_client_id;
    }

    public void setOpenid_client_id(String openid_client_id) {
        this.openid_client_id = openid_client_id;
    }
}
