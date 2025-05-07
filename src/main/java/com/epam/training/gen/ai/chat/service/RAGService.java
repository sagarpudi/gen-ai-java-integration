package com.epam.training.gen.ai.chat.service;

import com.epam.training.gen.ai.openai.OpenAIClient;
import com.epam.training.gen.ai.qdrant.QdrantClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RAGService {
    @Autowired
    private OpenAIClient openAIClient;
    @Autowired private QdrantClient qdrantClient;

    public String getAnswer(String query) {
        List<Double> queryEmbedding = openAIClient.getEmbedding(query);
        List<String> context = qdrantClient.searchRelevantChunks(queryEmbedding);
        String prompt = "Answer the question using context:\n" + String.join("\n", context) + "\nQ: " + query;
        return openAIClient.getCompletion(prompt);
    }
}
