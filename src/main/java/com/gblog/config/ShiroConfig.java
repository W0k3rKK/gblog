package com.gblog.config;

import com.gblog.shiro.AccountRealm;
import com.gblog.shiro.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: W0k3rKK
 * @description shiro启用拦截控制器
 * @date: 2023/3/23 18:54
 */
@Configuration
public class ShiroConfig {

	@Autowired
	private JwtFilter jwtFilter;

	/**
	 * 注入sessionManager
	 *
	 * @param redisSessionDAO
	 * @return
	 */
	@Bean
	public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {

		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

		sessionManager.setSessionDAO(redisSessionDAO);

		return sessionManager;
	}

	/**
	 * 注入securityManager
	 *
	 * @param accountRealm
	 * @param sessionManager
	 * @param redisCacheManager
	 * @return
	 */
	@Bean
	public DefaultWebSecurityManager securityManager(AccountRealm accountRealm,
	                                                 SessionManager sessionManager,
	                                                 RedisCacheManager redisCacheManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

		securityManager.setSessionManager(sessionManager);

		securityManager.setCacheManager(redisCacheManager);
		return securityManager;
	}

	/**
	 * 注入shiroFilter
	 * @return
	 */
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

		Map<String, String> filterMap = new LinkedHashMap<>();

		filterMap.put("/**", "jwt");
		chainDefinition.addPathDefinitions(filterMap);
		return chainDefinition;
	}

	/**
	 * shiro
	 * @param securityManager
	 * @param shiroFilterChainDefinition
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
	                                                     ShiroFilterChainDefinition shiroFilterChainDefinition) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);

		Map<String, Filter> filters = new HashMap<>();
		filters.put("jwt", jwtFilter);
		shiroFilter.setFilters(filters);

		Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();

		shiroFilter.setFilterChainDefinitionMap(filterMap);
		return shiroFilter;
	}


}
