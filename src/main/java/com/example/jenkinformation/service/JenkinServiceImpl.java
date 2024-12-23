package com.example.jenkinformation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;

@Service

public class JenkinServiceImpl implements  JenkinService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${jenkins.username}")
    private String username;

    @Value("${jenkins.password}")
    private String password;


    public JenkinServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode callJenkinsApi() {
        String url = "http://localhost:8080/api/json";

        // Basic Auth
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", createBasicAuthHeader(username, password));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode  rootNode =  objectMapper.readTree(response.getBody()); // Convert the response body to a JSON object
            // Extract the "jobs" property from the response
            return rootNode.path("jobs"); // Return only the "jobs" property
        } catch (Exception e) {
            // Return an error JSON object in case of exceptions
            return objectMapper.createObjectNode().put("error", e.getMessage());
        }
    }
    private String createBasicAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        return "Basic " + new String(encodedAuth);
    }
}
