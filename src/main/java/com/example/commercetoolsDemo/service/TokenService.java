package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.feign.CtAuthFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class TokenService {

    @Value("${ct.clientId}")
    private String clientId;

    @Value("${ct.clientSecret}")
    private String clientSecret;

    @Value("${ct.scopes}")
    private String scopes;

    private String accessToken;
    private Instant expiryTime;

    private final CtAuthFeignClient authFeignClient;

    public TokenService(CtAuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    /**
     * Returns cached admin token or fetches a new one.
     */
    public synchronized String getAdminToken() {

        if (accessToken == null || isTokenExpired()) {
            log.info("Admin token missing or expired, fetching new token");
            fetchNewToken();
        }

        return accessToken;
    }

    /**
     * Fetch new token using Feign (client_credentials flow).
     */
    private void fetchNewToken() {

        try {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");
            body.add("scope", scopes);

            String basicAuth = "Basic " + Base64.getEncoder()
                    .encodeToString((clientId + ":" + clientSecret)
                            .getBytes(StandardCharsets.UTF_8));

            Map<String, Object> response =
                    authFeignClient.getToken(basicAuth, "client_credentials",scopes);

            accessToken = (String) response.get("access_token");
            Integer expiresIn = (Integer) response.get("expires_in");

            if (accessToken == null || expiresIn == null) {
                throw new IllegalStateException("Invalid token response: " + response);
            }

            expiryTime = Instant.now().plusSeconds(expiresIn - 60);

            log.info("Admin token fetched successfully, expires in {} seconds", expiresIn);
            log.info("Admin token prefix: {}", accessToken.substring(0, 15));

        } catch (Exception ex) {
            log.error("Failed to fetch commercetools admin token", ex);
            throw ex;
        }
    }

    private boolean isTokenExpired() {
        return expiryTime == null || Instant.now().isAfter(expiryTime);
    }
}
