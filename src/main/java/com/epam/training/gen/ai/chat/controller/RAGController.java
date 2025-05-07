package com.epam.training.gen.ai.chat.controller;

import com.epam.training.gen.ai.chat.service.FileService;
import com.epam.training.gen.ai.chat.service.RAGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class RAGController {
    @Autowired
    private FileService fileService;
    @Autowired
    private RAGService ragService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
        fileService.processFile(file);
        return ResponseEntity.ok("Knowledge uploaded.");
    }

    @GetMapping("/ask")
    public ResponseEntity<String> ask(@RequestParam String question) {
        return ResponseEntity.ok(ragService.getAnswer(question));
    }
}
