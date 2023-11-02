package co.jp.vmware.tanzu.explore.api.config;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.ai.openai.client.OpenAiClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;

public class MyOpenAiClient extends OpenAiClient {

    private final OpenAiService openAiService;

    public MyOpenAiClient(OpenAiService openAiService) {
        super(openAiService);
        this.openAiService = openAiService;
    }

    public void sseCompletion(SseEmitter emitter, String prompt, Integer maxTokens) {

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
            openAiService
                    .streamCompletion(completionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .doOnComplete(emitter::complete)
                    .blockingForEach(chunk -> {
                        String text = chunk.getChoices().get(0).getText();
                        if (text == null) {
                            return;
                        }
                        emitter.send(
                                SseEmitter.event().name("summarize").data(text)
                        );
                    });
    }
}