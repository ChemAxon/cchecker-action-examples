package com.chemaxon.ccapiclient.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chemaxon.ccapiclient.props.AppProps;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;

/**
 * Sets authentication header for requests sent by feign clients
 */
@Configuration
public class FeignAuthConfig {
    
    @Bean
    public RequestInterceptor basicAuthRequestInterceptor(AppProps config) {
        if (config.getSecurityProviderName().equals("ANONYMOUS")) {
            return requestTemplate -> {};
        }
        return new BasicAuthRequestInterceptor(config.getUsername(), config.getPassword());
    }
}