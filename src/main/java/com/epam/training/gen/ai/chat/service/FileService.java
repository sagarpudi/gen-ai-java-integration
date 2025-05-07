package com.epam.training.gen.ai.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private EmbeddingService embeddingService;
    public void processFile(MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        List<String> chunks = Arrays.asList(content.split("(?<=\n\n)"));
        embeddingService.embedAndStore(chunks);
    }
}
