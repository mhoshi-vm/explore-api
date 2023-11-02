package co.jp.vmware.tanzu.explore.api.repository;

import co.jp.vmware.tanzu.explore.api.record.FullText;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface FullTextRepo extends ListCrudRepository<FullText, Integer> {
    List<FullText> findBySessionIdAndSequence(String sessionId, Integer sequence);
}
