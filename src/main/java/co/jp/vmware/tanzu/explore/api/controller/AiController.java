package co.jp.vmware.tanzu.explore.api.controller;

import co.jp.vmware.tanzu.explore.api.record.FullText;
import co.jp.vmware.tanzu.explore.api.record.Summary;
import co.jp.vmware.tanzu.explore.api.service.FullTextService;
import co.jp.vmware.tanzu.explore.api.service.SummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AiController {


    SummaryService summaryService;

    FullTextService fullTextService;

    public AiController(SummaryService summaryService, FullTextService fullTextService) {
        this.summaryService = summaryService;
        this.fullTextService = fullTextService;
    }

    @GetMapping("/search")
    public List<Summary> getSummaries() {
        return summaryService.get();
    }

    @GetMapping("/summarize")
    public List<FullText> getFullText() {
        return fullTextService.get();
    }

}
