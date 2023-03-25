package com.gblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author: W0k3rKK
 * @description 定义用于shiro支持jwt
 * @date: 2023/3/24 17:47
 */
public class JwtToken implements AuthenticationToken {

	private String token;

	public JwtToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}
