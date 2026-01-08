package com.example.commercetoolsDemo.service;

import com.example.api.model.*;
//import com.example.commercetoolsDemo.dto.response.CtTokenResponse;
import com.example.commercetoolsDemo.dto.CtTokenResponse;
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

        log.debug("TokenService#getAdminToken returning token");
        return accessToken;
    }

    /**
     * Fetch new token using client_credentials flow.
     */
    private void fetchNewToken() {

        try {
            String basicAuth = "Basic " + Base64.getEncoder()
                    .encodeToString((clientId + ":" + clientSecret)
                            .getBytes(StandardCharsets.UTF_8));

            CtTokenResponse response =
                    authFeignClient.getToken(basicAuth, "client_credentials", scopes);

            if (response == null || response.getAccessToken() == null) {
                throw new IllegalStateException("Invalid token response from commercetools");
            }

            this.accessToken = response.getAccessToken();
            this.expiryTime = Instant.now()
                    .plusSeconds(response.getExpiresIn() - 60);

            log.info(
                    "Admin token fetched successfully | expiresIn={}s | scope={}",
                    response.getExpiresIn(),
                    response.getScope()
            );

            log.debug("Admin token prefix: {}", accessToken.substring(0, 15));

        } catch (Exception ex) {
            log.error("Failed to fetch commercetools admin token", ex);
            throw ex;
        } finally {
            log.debug("TokenService#fetchNewToken finished");
        }
    }

    private boolean isTokenExpired() {
        boolean expired = expiryTime == null || Instant.now().isAfter(expiryTime);
        log.debug("TokenService#isTokenExpired = {}", expired);
        return expired;
    }
}
