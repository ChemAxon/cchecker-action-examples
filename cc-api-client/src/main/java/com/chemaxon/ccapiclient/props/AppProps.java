package com.chemaxon.ccapiclient.props;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class AppProps {

    @Value("${common.systemUser:system}")
    private String username;

    @Value("${common.systemPassword:system}")
    private String password;

    @Value("${common.securityProviderName:IN_MEMORY}")
    private String securityProviderName;

    @Value("${check.chunk-size:10}")
    private int chunkSize;

    @Value("#{'${cc.check.param.country-codes}'.split(',')}")
    private Set<String> categoryGroupIds;

    @Value("${sql.select}")
    private String select;

    @Value("${check.max_thread_pool_size:10}")
    private int threadPoolSize;
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityProviderName() {
        return securityProviderName;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public Set<String> getCategoryGroupIds() {
        return categoryGroupIds;
    }

    public String getSelect() {
        return select;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
