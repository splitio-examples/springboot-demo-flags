package com.example.demo;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationsAPIConfig {

    @Value("#{ @environment['split.api.key'] }")
    private String apiToken;

    @Bean
    public SplitClient splitClient() throws Exception {
        SplitClientConfig config = SplitClientConfig.builder()
            .setBlockUntilReadyTimeout(20000)
            .enableDebug()
            .build();

        SplitFactory splitFactory = SplitFactoryBuilder.build(apiToken, config);
        SplitClient client = splitFactory.client();
        client.blockUntilReady();

        return client;
    }
}
