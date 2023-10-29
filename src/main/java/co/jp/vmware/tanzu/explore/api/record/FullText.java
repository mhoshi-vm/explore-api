package co.jp.vmware.tanzu.explore.api.record;

import org.springframework.data.annotation.Id;

public record FullText(@Id Integer id, String sessionId, Integer sequence, String context) {
}
