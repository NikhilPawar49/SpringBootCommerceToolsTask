package com.example.commercetoolsDemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
public class TokenService {

    @Value("${ct.authUrl}")
    private String authUrl;

    @Value("${ct.clientId}")
    private String clientId;

    @Value("${ct.clientSecret}")
    private String clientSecret;

    @Value("${ct.scopes}")
    private String scopes;

    private String accessToken;
    private Instant expiryTime;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Returns a valid admin token (cached or fresh).
     */
    public synchronized String getAdminToken() {

        if (accessToken == null || isTokenExpired()) {
            log.info("Admin token missing or expired, fetching new token");
            fetchNewToken();
        }

        return accessToken;
    }

    /**
     * Calls commercetools OAuth server using client_credentials flow.
     */
    private void fetchNewToken() {

        try {
            log.info("Calling commercetools OAuth server");

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");
            body.add("scope", scopes);

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    authUrl + "/oauth/token",
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getBody() == null) {
                throw new IllegalStateException("Empty token response from commercetools");
            }

            Map<String, Object> responseBody = response.getBody();

            accessToken = (String) responseBody.get("access_token");
            Integer expiresIn = (Integer) responseBody.get("expires_in");

            if (accessToken == null || expiresIn == null) {
                throw new IllegalStateException("Invalid token response: " + responseBody);
            }

            // safety buffer of 60 seconds
            expiryTime = Instant.now().plusSeconds(expiresIn - 60);

            log.info("Admin token fetched successfully, expires in {} seconds", expiresIn);
            log.info("Admin token prefix: {}", accessToken.substring(0, 15));

        } catch (Exception ex) {
            log.error("Failed to fetch commercetools admin token", ex);
            throw ex;
        }
    }

    /**
     * Checks if cached token is expired.
     */
    private boolean isTokenExpired() {
        return expiryTime == null || Instant.now().isAfter(expiryTime);
    }
}
