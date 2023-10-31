package co.jp.vmware.tanzu.explore.api.config;

import com.theokanning.openai.completion.CompletionChunk;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.openai.client.OpenAiClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.SystemMessage;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;
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
        this.openAiService.createChatCompletion(chatCompletionRequest)
                .getChoices()
                .forEach(choice -> builder.append(choice.getMessage().getContent()));

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


    public void streamChat(SseEmitter sseEmitter, String text, Integer maxTokens) {
        List<ChatMessage> chatMessages = List.of(new ChatMessage("user", text));
        logger.trace("ChatMessages: ", chatMessages);


        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(super.getModel())
                .temperature(super.getTemperature())
                .messages(List.of(new ChatMessage("user", text)))
                .stream(true)
                .maxTokens(3000)
                .n(1)
                .logitBias(new HashMap<>())
                .build();

        // https://github.com/TheoKanning/openai-java/issues/266
        openAiService.streamChatCompletion(chatCompletionRequest).subscribe(chunk -> {
            String intext = "aaaaa";
            if (intext == null) {
                return;
            }
                System.out.printf(intext);
        }, Throwable::printStackTrace, openAiService::shutdownExecutor);
    }
}