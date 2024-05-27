package com.lcwd.user.service.config;

import com.lcwd.user.service.config.interceptor.RestTemplateInterceptors;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new RestTemplateInterceptors());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    public OAuth2AuthorizedClientManager manager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository
    ) {
        // Build an OAuth2AuthorizedClientProvider for client credentials grant type
        OAuth2AuthorizedClientProvider Provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        // Create a DefaultOAuth2AuthorizedClientManager and set the client provider
        DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, auth2AuthorizedClientRepository
        );
        defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(Provider);

        return defaultOAuth2AuthorizedClientManager;
    }

}
