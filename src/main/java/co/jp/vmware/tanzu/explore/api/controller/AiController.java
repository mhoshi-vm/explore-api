package co.jp.vmware.tanzu.explore.api.controller;

import co.jp.vmware.tanzu.explore.api.config.MyOpenAiClient;
import co.jp.vmware.tanzu.explore.api.record.FullText;
import co.jp.vmware.tanzu.explore.api.record.Summary;
import co.jp.vmware.tanzu.explore.api.service.FullTextService;
import co.jp.vmware.tanzu.explore.api.service.SummaryService;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
public class AiController {


    SummaryService summaryService;

    MyOpenAiClient openAiClient;

    FullTextService fullTextService;

    public AiController(SummaryService summaryService, MyOpenAiClient myOpenAiClient, FullTextService fullTextService) {
        this.summaryService = summaryService;
        this.openAiClient = myOpenAiClient;
        this.fullTextService = fullTextService;
    }

    @GetMapping("/search")
    public List<Summary> getSummaries(@RequestParam(defaultValue = "aaa") String prompt, @RequestParam(defaultValue = "20") Integer limit) {
        return summaryService.get(prompt, limit);
    }

    @GetMapping("/summarize")
    public SseEmitter streamChat(@RequestParam(defaultValue = "tell me a joke!") String user) {

        SseEmitter emitter = new SseEmitter(0L);/*
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                openAiClient.streamChat(emitter, user, 4000);
                //emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });*/
        return emitter;
    }

}
