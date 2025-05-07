package com.epam.training.gen.ai.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OpenAIClient {

    @Value("${client.azureopenai.key}")
    private String apiKey;

    public List<Double> getEmbedding(String text) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://api.openai.com/v1/embeddings");
            post.setHeader("Authorization", "Bearer " + apiKey);
            post.setHeader("Content-Type", "application/json");

            String body = String.format("{\"model\":\"text-embedding-ada-002\",\"input\":%s}", new ObjectMapper().writeValueAsString(text));
            post.setEntity(new StringEntity(body));

            try (CloseableHttpResponse response = client.execute(post)) {
                String json = EntityUtils.toString(response.getEntity());
                JsonNode root = new ObjectMapper().readTree(json);
                JsonNode embedding = root.get("data").get(0).get("embedding");
                return new ObjectMapper().convertValue(embedding, new TypeReference<List<Double>>() {});
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to get embedding", e);
        }
    }

    public String getCompletion(String prompt) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
            post.setHeader("Authorization", "Bearer " + apiKey);
            post.setHeader("Content-Type", "application/json");

            String body = "{" +
                    "\"model\": \"gpt-3.5-turbo\"," +
                    "\"messages\": [{\"role\": \"user\", \"content\": " + new ObjectMapper().writeValueAsString(prompt) + "}]" +
                    "}";
            post.setEntity(new StringEntity(body));

            try (CloseableHttpResponse response = client.execute(post)) {
                String json = EntityUtils.toString(response.getEntity());
                JsonNode root = new ObjectMapper().readTree(json);
                return root.get("choices").get(0).get("message").get("content").asText();
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to get completion", e);
        }
    }
}
