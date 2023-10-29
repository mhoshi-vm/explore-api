package co.jp.vmware.tanzu.explore.api.config;

import co.jp.vmware.tanzu.explore.api.client.MyOpenAiApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.ai.autoconfigure.openai.OpenAiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class MyOpenAiConfig {

    @Bean
    public OpenAiService myOpenAiService(OpenAiProperties openAiProperties) {

        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(openAiProperties.getApiKey(), openAiProperties.getDuration());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(openAiProperties.getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        OpenAiApi api = retrofit.create(MyOpenAiApi.class);

        return new OpenAiService(api);
    }

    @Bean
    public MyOpenAiClient myOpenAiClient(OpenAiProperties openAiProperties, OpenAiService myOpenAiService) {
        MyOpenAiClient openAiClient = new MyOpenAiClient(myOpenAiService);
        openAiClient.setTemperature(openAiProperties.getTemperature());
        openAiClient.setModel(openAiProperties.getModel());
        return openAiClient;
    }
}