package edu.asu.conceptpower.app.core.impl;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
class IndexerAsyncConfig implements AsyncConfigurer {

    @Override
    @Bean(name = "indexExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(1);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setBeanName("indexExecutor");
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // return new AsyncExceptionHandler();
        return null;
    }

}
