package com.epam.training.gen.ai.chat.util;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public enum DeploymentModel {
    GPT_3_5_TURBO("gpt-35-turbo-1106"),
    GPT_4("gpt-4"),
    GOOGLE_CHAT_BISON("chat-bison@001");
    private final String modelName;
    DeploymentModel(String modelName) {
        this.modelName = modelName;
    }

    public static Stream<DeploymentModel> valuesAsStream() {
        return Arrays.stream(DeploymentModel.values());
    }

    public String getModelName() {
        return modelName;
    }
}

