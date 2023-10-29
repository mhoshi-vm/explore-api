package co.jp.vmware.tanzu.explore.api.repository;

import co.jp.vmware.tanzu.explore.api.record.Summary;
import org.springframework.data.repository.ListCrudRepository;

public interface SummaryRepo extends ListCrudRepository<Summary, Integer> {
}
