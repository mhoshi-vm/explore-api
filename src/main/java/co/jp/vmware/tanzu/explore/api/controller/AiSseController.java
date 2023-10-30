package co.jp.vmware.tanzu.explore.api.controller;

import co.jp.vmware.tanzu.explore.api.service.FullTextService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class AiSseController {

    FullTextService fullTextService;

    public AiSseController(FullTextService fullTextService) {
        this.fullTextService = fullTextService;
    }
}
