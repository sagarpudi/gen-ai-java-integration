package com.epam.training.gen.ai.chat.service;

import com.epam.training.gen.ai.chat.model.PromptResponse;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;

public interface PromptService {
    PromptResponse getChatBotResponse(String userPrompt, PromptExecutionSettings promptExecutionSettings);
}
