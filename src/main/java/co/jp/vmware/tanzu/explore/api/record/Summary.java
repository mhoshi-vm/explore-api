package co.jp.vmware.tanzu.explore.api.record;

import org.springframework.data.annotation.Id;

public record Summary(@Id Integer id, String sessionId, String context) {
}
