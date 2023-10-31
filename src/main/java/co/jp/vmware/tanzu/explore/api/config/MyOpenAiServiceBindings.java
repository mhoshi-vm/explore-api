package co.jp.vmware.tanzu.explore.api.config;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class MyOpenAiServiceBindings implements BindingsPropertiesProcessor {

    public static final String TYPE = "openai";

    @Override
    public void process(Environment environment, Bindings bindings, Map<String, Object> properties) {

        List<Binding> myBindings = bindings.filterBindings(TYPE);
        if (myBindings.isEmpty()) {
            return;
        }

        properties.put("spring.ai.openai.api-key", myBindings.get(0).getSecret().get("api-key"));
        properties.put("spring.ai.openai.model", myBindings.get(0).getSecret().get("model"));
        properties.put("spring.ai.openai.base-url", myBindings.get(0).getSecret().get("url"));
        properties.put("spring.ai.openai.embedding-model", myBindings.get(0).getSecret().get("embedding-models"));

    }

}