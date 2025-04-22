package com.epam.training.gen.ai.chat.service;

import com.epam.training.gen.ai.chat.model.PromptResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import com.microsoft.semantickernel.semanticfunctions.OutputVariable;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class PromptServiceImpl implements PromptService {

    private ChatCompletionService dynamicChatCompletionService;
    private final Kernel semanticKernel;
    private InvocationContext invocationContext;

    @Override
    public PromptResponse getChatBotResponse(String userPrompt, PromptExecutionSettings promptExecutionSettings) {
        try {
            // Define the prompt template
            KernelFunction<String> genericFunction = KernelFunctionFromPrompt.builder()
                    .withTemplate("""
                You are an intelligent assistant with access to the following functions:
                - getWeatherForecast(city): Returns weather info
                - calculateAge(birthYear): Returns person's age
                
                Based on the user's request, call the appropriate function automatically.
                
                User asked: {{$input}}
            """)
                    .withName("AutoFunctionCaller")
                    .withOutputVariable(new OutputVariable<>("result", String.class))
                    .build();

            // Register the function as a plugin
            Map<String, KernelFunction<?>> functions = new HashMap<>();
            functions.put("AutoFunctionCaller", genericFunction);
            KernelPlugin kernelPlugin = new KernelPlugin("PromptPlugin", "plugins for prompt", functions);

            // Build the updated Kernel with the plugin
            Kernel updatedKernel = semanticKernel.toBuilder()
                    .withPlugin(kernelPlugin)
                    .build();

            // Set up the invocation context
            InvocationContext invocationContext = InvocationContext.builder()
                    .withPromptExecutionSettings(promptExecutionSettings)
                    .build();

            // Invoke the function using the Kernel
            FunctionResult<String> result = updatedKernel.invoke(genericFunction);
            String output = result.getResult();

            // Return the response
            return PromptResponse.builder()
                    .userPrompt(userPrompt)
                    .chatBotResponse(output)
                    .build();

        } catch (Exception e) {
            log.error("Error during function call", e);
            return PromptResponse.builder()
                    .userPrompt(userPrompt)
                    .chatBotResponse("Oops, something went wrong!")
                    .build();
        }
    }

}

