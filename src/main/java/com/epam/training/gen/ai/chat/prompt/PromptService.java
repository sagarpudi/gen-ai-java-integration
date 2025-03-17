package com.epam.training.gen.ai.chat.prompt;

import com.epam.training.gen.ai.chat.model.PromptResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PromptService {
    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final InvocationContext invocationContext;

    public PromptResponse getChatBotResponse(String userPrompt) {

        ChatHistory history = new ChatHistory();
        history.addUserMessage(userPrompt);
        List<ChatMessageContent<?>> results = chatCompletionService
                .getChatMessageContentsAsync(history, null, invocationContext)
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
}