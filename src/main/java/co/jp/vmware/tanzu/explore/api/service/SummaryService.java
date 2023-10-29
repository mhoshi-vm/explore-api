package co.jp.vmware.tanzu.explore.api.service;

import co.jp.vmware.tanzu.explore.api.record.Summary;
import co.jp.vmware.tanzu.explore.api.repository.SummaryRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryService {

    SummaryRepo summaryRepo;

    public SummaryService(SummaryRepo summaryRepo) {
        this.summaryRepo = summaryRepo;
    }

    public List<Summary> get() {
        // Do Vector Search
        return summaryRepo.findAll();
    }
}
