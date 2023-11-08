package co.jp.vmware.tanzu.explore.api.config;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import org.springframework.ai.openai.client.OpenAiClient;

import java.util.List;

public class MyOpenAiClient extends OpenAiClient {

    private final OpenAiService openAiService;

    public MyOpenAiClient(OpenAiService openAiService) {
        super(openAiService);
        this.openAiService = openAiService;
    }

    public Flowable<ChatCompletionChunk> streamCompletion(String prompt) {

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(super.getModel())
                .temperature(super.getTemperature())
                .messages(List.of(new ChatMessage("user", prompt)))
                .build();

        return openAiService.streamChatCompletion(completionRequest);
    }
}