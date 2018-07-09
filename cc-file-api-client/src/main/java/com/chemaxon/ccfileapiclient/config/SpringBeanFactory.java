package com.chemaxon.ccfileapiclient.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.chemaxon.ccfileapiclient.service.CheckResultProcessorService;
import com.chemaxon.ccfileapiclient.service.CsvInputFileCreatorService;
import com.chemaxon.ccfileapiclient.service.impl.CheckResultProcessorJpaService;
import com.chemaxon.ccfileapiclient.service.impl.CsvInputFileCreatorJpaService;

@Configuration
public class SpringBeanFactory {

    @Bean
    RestTemplate jsonRestTemplate(RestTemplateBuilder builder, @Value("${bigdata.user:admin}") String user,
            @Value("${bigdata.password:password}") String password) {
        return builder
                .basicAuthorization(user, password)
                .interceptors((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    return execution.execute(request, body);
                })
                .additionalMessageConverters(jacksonSupportsOcetStream())
                .build();
    }
    
    private HttpMessageConverter<?> jacksonSupportsOcetStream() { 
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        return converter;
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${bigdata.user:admin}") String user,
            @Value("${bigdata.password:cxn123}") String password) {
        return builder
                .basicAuthorization(user, password)
                .build();
    }

    // Change the implementation of this interface to read and create input csv files from real data source   
    @Bean
    CsvInputFileCreatorService molFileCreatorService() {
        return new CsvInputFileCreatorJpaService();
    }
    
    // Change the implementation of this interface to persist check results into a real data source
    @Bean
    CheckResultProcessorService checkResultProcessorService() {
        return new CheckResultProcessorJpaService();
    }
}
