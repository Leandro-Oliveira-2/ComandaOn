package com.lanchonete.lanchon.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {
    /**
     * Recomendo Base64 de pelo menos 256 bits (32 bytes) para HS256.
     * Ex.: "q7bD7i6v...==" (Base64)
     */
    private String secret;
    private String issuer = "lanchon-api";
    private long expiresInSeconds = 86400; // 24h

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public long getExpiresInSeconds() { return expiresInSeconds; }
    public void setExpiresInSeconds(long expiresInSeconds) { this.expiresInSeconds = expiresInSeconds; }
}
