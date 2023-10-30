package co.jp.vmware.tanzu.explore.api.service;

import co.jp.vmware.tanzu.explore.api.config.MyOpenAiClient;
import co.jp.vmware.tanzu.explore.api.record.FullText;
import co.jp.vmware.tanzu.explore.api.repository.FullTextRepo;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
public class FullTextService {

    FullTextRepo fullTextRepo;

    MyOpenAiClient myOpenAiClient;

    public FullTextService(FullTextRepo fullTextRepo, MyOpenAiClient myOpenAiClient) {
        this.fullTextRepo = fullTextRepo;
        this.myOpenAiClient = myOpenAiClient;
    }




    private void streamReponse(SseEmitter sseEmitter){

    }

    public List<FullText> get() {
        return fullTextRepo.findAll();
    }
}
