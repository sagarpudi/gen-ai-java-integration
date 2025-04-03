package com.epam.training.gen.ai.chat.controller;

import com.epam.training.gen.ai.chat.model.PromptResponse;
import com.epam.training.gen.ai.chat.service.PromptService;
import com.epam.training.gen.ai.chat.util.ValidationUtils;
import com.epam.training.gen.ai.configuration.PromptExecutionConfig;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class ChatBotController {

    private final PromptService promptService;
    @Autowired
    private PromptExecutionConfig promptExecutionConfig;


    public ChatBotController(PromptService promptService) {
        this.promptService = promptService;
    }

    @GetMapping("prompt")
    public Map<String, Object> prompt(@RequestParam(required = false) String userPrompt, @RequestParam(required = false) double temperature) {
        ValidationUtils.validateUserPrompt(userPrompt);
        ValidationUtils.validateTemperature(temperature);
        promptExecutionConfig = PromptExecutionConfig.builder().temperature(temperature)
                .maxTokensPerPrompt(promptExecutionConfig.getMaxTokensPerPrompt())
                .deploymentName( promptExecutionConfig.getDeploymentName())
                .build();

        log.debug("Prompt execution config: tokens - {},deployment- {}, temperature {} ",promptExecutionConfig.getMaxTokensPerPrompt(), promptExecutionConfig.getDeploymentName(), promptExecutionConfig.getTemperature());

        PromptResponse response = generateResponse(userPrompt,promptExecutionConfig);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user_prompt", userPrompt);
        responseMap.put("response", response);

        return responseMap;
    }

    @GetMapping("chat")
    public Map<String, Object> chat(@RequestParam(required = false) String prompt) {
        ValidationUtils.validateUserPrompt(prompt);

        log.debug("Prompt execution config: tokens - {},deployment- {}, temperature {} ",promptExecutionConfig.getMaxTokensPerPrompt(), promptExecutionConfig.getDeploymentName(), promptExecutionConfig.getTemperature());

        PromptResponse response = generateResponse(prompt, promptExecutionConfig);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("prompt", prompt);
        responseMap.put("response", response);

        return responseMap;
    }

    private PromptResponse generateResponse(String prompt, PromptExecutionConfig promptExecutionConfig)  {
        return Optional.ofNullable(promptService)
                .map(promptService -> promptService.getChatBotResponse(prompt, PromptExecutionSettings.builder()
                        .withModelId(promptExecutionConfig.getDeploymentName())
                        .withTemperature(promptExecutionConfig.getTemperature())
                        .withMaxTokens(promptExecutionConfig.getMaxTokensPerPrompt())
                        .build()))
                .orElseGet(PromptResponse::new);

    }
}