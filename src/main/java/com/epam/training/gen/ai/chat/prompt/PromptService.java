package com.epam.training.gen.ai.chat.prompt;

import com.epam.training.gen.ai.chat.model.PromptResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.responseformat.ResponseFormat;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import com.microsoft.semantickernel.semanticfunctions.OutputVariable;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.training.gen.ai.chat.util.PromptConstants.*;

@Service
@AllArgsConstructor
public class PromptService {
    private ChatCompletionService dynamicChatCompletionService;
    private final Kernel semanticKernel;
    private InvocationContext invocationContext;

    @Value("${prompt.execution.temperature}")
    private double temperature;
    @Value("{prompt.execution.maxTokens}")
    private Integer maxTokens;
    @Value("${client-azureopenai-deployment-name}")
    private String deploymentName;

    public PromptResponse getChatBotResponse(String userPrompt) {

        ChatHistory history = new ChatHistory();
        history.addUserMessage(userPrompt);
        try {
             dynamicChatCompletionService = semanticKernel.getService(ChatCompletionService.class);
        } catch (ServiceNotFoundException e) {
            throw new RuntimeException(e);
        }
        // adding prompt template
        Map<String, KernelFunction<?>> functions = new HashMap<>();
        functions.put("GenerateResponse", setupPromptFunction(userPrompt));
        Kernel kernel = semanticKernel.toBuilder()
                .withPlugin(new KernelPlugin("PromptPlugin", "plugins for prompt", functions)).build();

        // adding PromptExecutionSetting
         invocationContext = InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withModelId(deploymentName)
                        .withTemperature(temperature)
                        .withMaxTokens(maxTokens)
                        .build()).build();

        List<ChatMessageContent<?>> results = dynamicChatCompletionService
                .getChatMessageContentsAsync(history, kernel, invocationContext)
                .block();
        String response =
                Optional.ofNullable(results).orElse(List.of()).stream()
                        .filter(
                                chatMessageContent ->
                                        chatMessageContent.getAuthorRole() == AuthorRole.ASSISTANT
                                                && chatMessageContent.getContent() != null)
                        .map(ChatMessageContent::getContent)
                        .findFirst()
                        .orElse("No Output, Something went wrong . . !");
        return PromptResponse.builder().userPrompt(userPrompt).chatBotResponse(response).build();
    }


    private KernelFunction<String> setupPromptFunction(String inputText) {
        return KernelFunctionFromPrompt.builder()
                .withTemplate(getPromptTemplate(inputText))
                .withName("GenerateResponse")
                .withOutputVariable(new OutputVariable<>("result", String.class))
                .build();
    }

    private String getPromptTemplate(String request) {
        if (request.toLowerCase().contains("explain")) {
            return EDUCATIONAL_TUTOR.formatted(request);
        } else if (request.toLowerCase().contains("story")) {
            return CREATIVE_WRITER.formatted(request);
        } else if (request.toLowerCase().contains("code")) {
            return EXPERIENCED_PROGRAMMER.formatted(request);
        } else {
            return GENERAL_ASSISTANT.formatted(request);
        }
    }
}