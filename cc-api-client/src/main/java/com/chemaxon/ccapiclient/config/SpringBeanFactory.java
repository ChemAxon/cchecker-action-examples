package com.chemaxon.ccapiclient.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.chemaxon.ccapiclient.props.AppProps;
import com.chemaxon.ccapiclient.resource.IdentifiedMolecule;
import com.chemaxon.ccapiclient.resource.Result;

@Configuration
public class SpringBeanFactory {
    
    @Bean
    BlockingQueue<IdentifiedMolecule> inputQueue(AppProps config) {
        return new ArrayBlockingQueue<>(config.getThreadPoolSize() * config.getChunkSize() * 100);
    }

    @Bean
    BlockingQueue<Result> resultQueue(AppProps config) {
        return new ArrayBlockingQueue<>(config.getThreadPoolSize());
    }

    @Bean
    ThreadPoolTaskExecutor taskExecutor(AppProps config) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // add one extra thread to the pool for loading the data from the database
        executor.setCorePoolSize(config.getThreadPoolSize() + 1);
        executor.setMaxPoolSize(config.getThreadPoolSize() + 1);
        executor.setThreadNamePrefix("cc_rdbms");
        executor.initialize();
        return executor;
    }
    
    @Bean
    IdentifiedMolecule idMolPoisonPill() {
        return new IdentifiedMolecule();
    }
    
    @Bean
    Result resultPoisonPill() {
        return new Result();
    }
    
}
