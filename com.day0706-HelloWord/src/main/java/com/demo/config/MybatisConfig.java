package com.demo.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean(name = "optimisticLockerInterceptor")
    public OptimisticLockerInterceptor getOptimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

    @Bean(name = "paginationInterceptor")
    public PaginationInterceptor getPaginationInterceptor(){
        return new PaginationInterceptor();
    }
}
