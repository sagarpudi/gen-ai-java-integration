package com.epam.training.gen.ai.chat.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PromptResponse {
    private String userPrompt;

    @JsonRawValue  // Keeps JSON structure intact
    private String chatBotResponse;
}