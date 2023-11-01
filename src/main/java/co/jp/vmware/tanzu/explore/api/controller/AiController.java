package co.jp.vmware.tanzu.explore.api.controller;

import co.jp.vmware.tanzu.explore.api.config.MyOpenAiClient;
import co.jp.vmware.tanzu.explore.api.record.Summary;
import co.jp.vmware.tanzu.explore.api.service.FullTextService;
import co.jp.vmware.tanzu.explore.api.service.SummaryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import retrofit2.http.Streaming;

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

    @CrossOrigin
    @GetMapping("/search")
    public List<Summary> getSummaries(@RequestParam(defaultValue = "aaa") String prompt, @RequestParam(defaultValue = "20") Integer limit) {
        return summaryService.get(prompt, limit);
    }

    @CrossOrigin
    @GetMapping("/summarize")
    public SseEmitter streamChat(@RequestParam(defaultValue = "tell me a joke!") String userText) {

        SseEmitter emitter = new SseEmitter(-1L);
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                openAiClient.streamChat(emitter, userText, 4000);
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

}
