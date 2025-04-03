package com.epam.training.gen.ai.chat.util;

public class ValidationUtils {
    private static final String EMPTY_PROMPT_MESSAGE = "Prompt cannot be empty";
    private static final String INVALID_TEMPERATURE_MESSAGE = "Temperature must be between 0 and 1";

    public static void validateUserPrompt(String userPrompt) {
        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            throw new IllegalArgumentException(EMPTY_PROMPT_MESSAGE);
        }
    }

    public static void validateTemperature(double temperature) {
        if (temperature < 0 || temperature > 1) {
            throw new IllegalArgumentException(INVALID_TEMPERATURE_MESSAGE);
        }
    }
}
