package com.epam.training.gen.ai.chat.service;

import com.epam.training.gen.ai.openai.OpenAIClient;
import com.epam.training.gen.ai.qdrant.QdrantClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {
    @Autowired
    private OpenAIClient openAIClient;
    @Autowired
    private QdrantClient qdrantClient;

    public void embedAndStore(List<String> chunks) {
        for (String chunk : chunks) {
            List<Double> embedding = openAIClient.getEmbedding(chunk);
            qdrantClient.storeEmbedding(chunk, embedding);
        }
    }
}
