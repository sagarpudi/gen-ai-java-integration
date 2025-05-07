package com.epam.training.gen.ai.qdrant;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class QdrantClient {

    @Value("${qdrant.host}")
    private String qdrantHost;

    private final String collectionName = "rag_collection";

    public void storeEmbedding(String text, List<Double> embedding) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(qdrantHost + "/collections/" + collectionName + "/points");
            post.setHeader("Content-Type", "application/json");

            String body = String.format("{" +
                            "\"points\": [{\"id\": %d, \"vector\": %s, \"payload\": {\"text\": %s}}]" +
                            "}", UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
                    new ObjectMapper().writeValueAsString(embedding),
                    new ObjectMapper().writeValueAsString(text));

            post.setEntity(new StringEntity(body));
            client.execute(post).close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store embedding in Qdrant", e);
        }
    }

    public List<String> searchRelevantChunks(List<Double> queryEmbedding) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(qdrantHost + "/collections/" + collectionName + "/points/search");
            post.setHeader("Content-Type", "application/json");

            String body = String.format("{" +
                    "\"vector\": %s,\"limit\":3,\"with_payload\":true" +
                    "}", new ObjectMapper().writeValueAsString(queryEmbedding));

            post.setEntity(new StringEntity(body));
            try (CloseableHttpResponse response = client.execute(post)) {
                String json = EntityUtils.toString(response.getEntity());
                JsonNode root = new ObjectMapper().readTree(json);
                List<String> results = new ArrayList<>();
                for (JsonNode result : root.get("result")) {
                    results.add(result.get("payload").get("text").asText());
                }
                return results;
            } catch (ParseException e) {
                throw new RuntimeException("Failed to parse embedding in Qdrant",e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to search in Qdrant", e);
        }
    }
}