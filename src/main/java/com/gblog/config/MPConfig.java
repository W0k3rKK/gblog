package com.gblog.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: W0k3rKK
 * @description mp分页
 * @date: 2023/3/22 23:37
 */

@Configuration
@EnableTransactionManagement
@MapperScan("com.gblog.dao")
public class MPConfig {
	@Bean
	public PaginationInnerInterceptor paginationInnerInterceptor() {
		return new PaginationInnerInterceptor();
	}
}
