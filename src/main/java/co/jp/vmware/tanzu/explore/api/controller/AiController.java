package co.jp.vmware.tanzu.explore.api.controller;

import co.jp.vmware.tanzu.explore.api.config.MyOpenAiClient;
import co.jp.vmware.tanzu.explore.api.record.FullText;
import co.jp.vmware.tanzu.explore.api.record.Summary;
import co.jp.vmware.tanzu.explore.api.service.FullTextService;
import co.jp.vmware.tanzu.explore.api.service.SummaryService;
import com.theokanning.openai.completion.CompletionChunk;
import io.reactivex.Flowable;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping(path = "/summarize", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<String> streamChat(@RequestParam String sessionId,
                                       @RequestParam(defaultValue = "1") Integer sequence,
                                       @RequestParam(defaultValue = "") String userText) {

        List<FullText> fullTexts = fullTextService.getSessionContent(sessionId, sequence);
        Message message = fullTextService.getPrompt(fullTexts.get(0).content(), userText);

        Flowable<CompletionChunk> result = openAiClient.streamCompletion(message.getContent(), 4000);

        return result.filter(completionChunk ->
                completionChunk.getChoices().get(0) != null).map(
                completionChunk -> completionChunk.getChoices().get(0).getText());
    }
}
