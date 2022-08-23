package org.osoriojulio.springcloud.msvc.auth;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @LoadBalanced
    @Bean
    WebClient webClien(){
        return WebClient.builder().build();
    }

}
