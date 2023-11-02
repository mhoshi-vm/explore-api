package co.jp.vmware.tanzu.explore.api.service;

import co.jp.vmware.tanzu.explore.api.record.FullText;
import co.jp.vmware.tanzu.explore.api.repository.FullTextRepo;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FullTextService {

    FullTextRepo fullTextRepo;

    private final Resource resource;

    public FullTextService(FullTextRepo fullTextRepo, @Value("classpath:/prompt/qa.st") Resource resource) {
        this.fullTextRepo = fullTextRepo;
        this.resource = resource;
    }

    public List<FullText> getSessionContent(String sessionId, Integer sequence) {
        return fullTextRepo.findBySessionIdAndSequence(sessionId,sequence);
    }


    public Message getPrompt(String content, String userText) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(resource);
        return systemPromptTemplate.createMessage(Map.of("content", content, "userText", userText));
    }
}
