package com.epam.training.gen.ai.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class DeploymentService {

    @Value("${client.azureopenai.endpoint}")
    private String apiUrl;

    @Value("${client.azureopenai.key")
    private String apiKey;

    private final RestTemplate restTemplate;

    public DeploymentService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getDeployments() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
