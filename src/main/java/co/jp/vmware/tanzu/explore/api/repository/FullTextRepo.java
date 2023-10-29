package co.jp.vmware.tanzu.explore.api.repository;

import co.jp.vmware.tanzu.explore.api.record.FullText;
import org.springframework.data.repository.ListCrudRepository;

public interface FullTextRepo extends ListCrudRepository<FullText, Integer> {
}
