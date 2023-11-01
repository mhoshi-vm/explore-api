package co.jp.vmware.tanzu.explore.api.config;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.ai.openai.client.OpenAiClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.HashMap;

public class MyOpenAiClient extends OpenAiClient {

    private final OpenAiService openAiService;

    public MyOpenAiClient(OpenAiService openAiService) {
        super(openAiService);
        this.openAiService = openAiService;
    }

    public void streamChat(SseEmitter emitter, String userText, Integer maxTokens) {


            String prompt = String.format("""
                <s>[INST] <<SYS>>
                Below is an instruction that describes a task. Write a response that appropriately completes the request.
                <</SYS>>

                %s [/INST]""", userText);

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
            openAiService.streamCompletion(completionRequest).subscribe(chunk -> {
                String text = chunk.getChoices().get(0).getText();
                if (text == null) {
                    return;
                }

                //emitter.send(text);

                System.out.println(text);
            });
    }
}