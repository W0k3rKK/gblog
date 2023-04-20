package com.gblog.config;

import com.gblog.springsecurity.AcsDeniedHandler;
import com.gblog.springsecurity.JwtAuthProvider;
import com.gblog.springsecurity.JwtAuthTokenFilter;
import com.gblog.springsecurity.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author: W0k3rKK
 * @description spring security配置类
 * @date: 2023/4/7 11:06
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private UnauthorizedHandler unauthorizedHandler;


	@Autowired
	private AcsDeniedHandler acsDeniedHandler;


	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("user").password("password").roles("USER").build());
		return manager;
	}


	/**
	 * 配置AuthenticationManager，否则无法自动装配
	 *
	 * @param authConfig
	 * @return
	 *
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}


	/**
	 * 配置密码加密器，否则无法自动装配
	 *
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 自定义过滤器，配置用于自动装配
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	@Bean
	public JwtAuthTokenFilter jwtAuthTokenFilter() throws Exception {
		return new JwtAuthTokenFilter();
	}

	/**
	 * 自定义认证器，配置用于自动装配
	 *
	 * @return
	 */
	@Bean
	public JwtAuthProvider JwtAuthProvider() {
		return new JwtAuthProvider();
	}


	/**
	 * 配置过滤器链,配置拦截规则,配置自定义过滤器,配置认证器
	 *
	 * @param httpSecurity
	 * @return
	 *
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
//		        基于token，不需要csrf
				.csrf().disable()
//				基于token，不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
//				登录注册不需要认证
				.antMatchers("/login", "/register").permitAll()
//				其他请求全部需要认证
				.anyRequest()
				.authenticated();

//		禁用缓存
		httpSecurity.headers().cacheControl();
//		自定义认证器(Provider)
		httpSecurity.authenticationProvider(JwtAuthProvider());
//		将自定义的过滤器加入到过滤器链中，并在UsernamePasswordAuthenticationFilter之前执行
		httpSecurity.addFilterBefore(jwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//		自定义认证失败处理类
		httpSecurity.exceptionHandling()
				.authenticationEntryPoint(unauthorizedHandler);

//		 自定义授权失败处理类
		httpSecurity.exceptionHandling()
				.accessDeniedHandler(acsDeniedHandler);

//		.............

		return httpSecurity.build();
	}
}
