package co.jp.vmware.tanzu.explore.api.record;

import org.springframework.data.annotation.Id;

public record FullText(@Id Integer id, String sessionId, String title, Integer sequence, String content) {
}
