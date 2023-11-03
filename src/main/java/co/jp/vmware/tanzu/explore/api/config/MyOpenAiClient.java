package co.jp.vmware.tanzu.explore.api.config;

import com.theokanning.openai.completion.CompletionChunk;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import org.springframework.ai.openai.client.OpenAiClient;

import java.util.HashMap;

public class MyOpenAiClient extends OpenAiClient {

    private final OpenAiService openAiService;

    public MyOpenAiClient(OpenAiService openAiService) {
        super(openAiService);
        this.openAiService = openAiService;
    }

    public Flowable<CompletionChunk> streamCompletion(String prompt, Integer maxTokens) {

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .model(super.getModel())
                    .temperature(super.getTemperature())
                    .prompt(prompt)
                    .stream(true)
                    .maxTokens(maxTokens)
                    .n(1)
                    .logitBias(new HashMap<>())
                    .build();
            // https://github.com/TheoKanning/openai-java/issues/266
            // https://github.com/TheoKanning/openai-java/pull/195#issuecomment-1491910765
           return openAiService.streamCompletion(completionRequest);
    }
}