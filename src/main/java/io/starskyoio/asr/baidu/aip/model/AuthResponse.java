package io.starskyoio.asr.baidu.aip.model;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String expiresIn;
    private String scope;
    private String sessionKey;
    private String sessionSecret;
    private String error;
    private String errorDescription;
}
