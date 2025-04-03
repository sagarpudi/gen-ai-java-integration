package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Azure OpenAI Async Client.
 * <p>
 * This configuration defines a bean that provides an asynchronous client
 * for interacting with the Azure OpenAI Service. It uses the Azure Key
 * Credential for authentication and connects to a specified endpoint.
 */
@Configuration
public class OpenAIConfiguration {

    private final String AZURE_CLIENT_KEY;
    private final String AZURE_OPENAI_SERVICE_CLIENT_ENDPOINT;

    public OpenAIConfiguration(@Value("${client.azureopenai.key}") String AZURE_CLIENT_KEY,
                               @Value("${client.azureopenai.endpoint}") String AZURE_OPENAI_SERVICE_CLIENT_ENDPOINT) {
        this.AZURE_CLIENT_KEY = AZURE_CLIENT_KEY;
        this.AZURE_OPENAI_SERVICE_CLIENT_ENDPOINT = AZURE_OPENAI_SERVICE_CLIENT_ENDPOINT;
    }

    /**
     * Creates an {@link OpenAIAsyncClient} bean for interacting with Azure OpenAI Service asynchronously.
     *
     * @return an instance of {@link OpenAIAsyncClient}
     */
    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(AZURE_CLIENT_KEY))
                .endpoint(AZURE_OPENAI_SERVICE_CLIENT_ENDPOINT)
                .buildAsyncClient();
    }
}
