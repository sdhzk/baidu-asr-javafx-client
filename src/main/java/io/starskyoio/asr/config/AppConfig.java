package io.starskyoio.asr.config;

import io.starskyoio.asr.baidu.aip.client.AsrProClient;
import io.starskyoio.asr.baidu.aip.client.AuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AppConfig {
    @Value("${baidu.aip.apiKey}")
    private String apiKey;

    @Value("${baidu.aip.secretKey}")
    private String secretKey;

    @Value("${baidu.aip.apiUrl}")
    private String apiUrl;

    @Value("${baidu.aip.tokenUrl}")
    private String tokenUrl;

    @Bean
    public AuthClient authClient() {
        System.out.println(tokenUrl);
        return new AuthClient(tokenUrl, apiKey, secretKey);
    }

    @Bean
    public AsrProClient asrProClient() {
        return new AsrProClient(apiUrl);
    }

    @Bean
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(99999);
        executor.setThreadNamePrefix("async-service-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
