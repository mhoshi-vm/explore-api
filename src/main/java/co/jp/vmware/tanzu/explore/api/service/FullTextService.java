package co.jp.vmware.tanzu.explore.api.service;

import co.jp.vmware.tanzu.explore.api.record.FullText;
import co.jp.vmware.tanzu.explore.api.repository.FullTextRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullTextService {

    FullTextRepo fullTextRepo;

    public FullTextService(FullTextRepo fullTextRepo) {
        this.fullTextRepo = fullTextRepo;
    }

    public List<FullText> get() {
        return fullTextRepo.findAll();
    }
}
