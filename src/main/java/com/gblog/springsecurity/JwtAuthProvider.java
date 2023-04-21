package com.gblog.springsecurity;

import com.gblog.service.impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author: W0k3rKK
 * @description 自定义认证器(Provider)
 *              <p>获取保存的用户凭证，自定义比较逻辑，返回认证结果</p>
 * @date: 2023/4/7 14:43
 */
@Component
public class JwtAuthProvider implements AuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserDetailService userDetailsService;


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = String.valueOf(authentication.getPrincipal());
		String password = String.valueOf(authentication.getCredentials());

		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(userName, password, userDetails.getAuthorities());
		}

		throw new BadCredentialsException("错误!!! Bad Credentials");
	}


	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
}
