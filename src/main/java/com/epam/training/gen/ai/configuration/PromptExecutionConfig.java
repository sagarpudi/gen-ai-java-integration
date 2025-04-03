package com.epam.training.gen.ai.configuration;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Builder
@Configuration
public class PromptExecutionConfig {

    private final double temperature;
    private final int maxTokensPerPrompt;
    private final String deploymentName;

    public PromptExecutionConfig(
            @Value("${prompt.execution.temperature}") double temperature,
            @Value("${prompt.execution.maxTokens}") int maxTokensPerPrompt,
            @Value("${client.azureopenai.deploymentname}") String deploymentName) {
        this.temperature = temperature;
        this.maxTokensPerPrompt = maxTokensPerPrompt;
        this.deploymentName = deploymentName;
    }

}