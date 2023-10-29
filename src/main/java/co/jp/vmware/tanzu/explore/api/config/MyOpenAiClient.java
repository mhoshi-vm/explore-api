package co.jp.vmware.tanzu.explore.api.config;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.client.OpenAiClient;

import java.util.List;

public class MyOpenAiClient extends OpenAiClient {

    private static final Logger logger = LoggerFactory.getLogger(MyOpenAiClient.class);
    private final OpenAiService openAiService;

    public MyOpenAiClient(OpenAiService openAiService) {
        super(openAiService);
        this.openAiService = openAiService;
    }


    public String generate(String text, Integer maxTokens) {
        ChatCompletionRequest chatCompletionRequest = this.getChatCompletionRequest(text, maxTokens);
        return this.getResponse(chatCompletionRequest);
    }

    private String getResponse(ChatCompletionRequest chatCompletionRequest) {
        StringBuilder builder = new StringBuilder();
        this.openAiService.createChatCompletion(chatCompletionRequest).getChoices().forEach(choice -> builder.append(choice.getMessage().getContent()));

        return builder.toString();
    }

    private ChatCompletionRequest getChatCompletionRequest(String text, Integer maxTokens) {
        List<ChatMessage> chatMessages = List.of(new ChatMessage("user", text));
        logger.trace("ChatMessages: ", chatMessages);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(super.getModel())
                .temperature(super.getTemperature())
                .messages(List.of(new ChatMessage("user", text)))
                .maxTokens(maxTokens)
                .build();
        logger.trace("ChatCompletionRequest: ", chatCompletionRequest);
        return chatCompletionRequest;
    }

}