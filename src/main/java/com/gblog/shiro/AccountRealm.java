package com.gblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gblog.po.User;
import com.gblog.service.UserService;
import com.gblog.utils.JwtTokenUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023/3/24 19:45
 */
@Component
public class AccountRealm extends AuthorizingRealm {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;


	/**
	 * 让realm支持jwt的凭证校验
	 *
	 * @param token the token being submitted for authentication.
	 * @return
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	/**
	 * 为当前登录的Subject授予角色和权限
	 *
	 * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/**
	 * 验证当前登录的Subject
	 *
	 * @param token the authentication token containing the user's principal and credentials.
	 * @return
	 *
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		JwtToken jwtToken = (JwtToken) token;
		String userId = jwtTokenUtil.getClaimByToken(
				(String) jwtToken.getPrincipal()).getSubject();
		User user = userService.getById(Long.valueOf(userId));

		if (ObjectUtil.isNull(user)) {
			throw new AuthenticationException("账户不存在");
		}

		if (user.getStatus() == -1) {
			throw new AuthenticationException("账户已被锁定");
		}

		AccountProfile profile = new AccountProfile();
		BeanUtil.copyProperties(user, profile);

		return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
	}
}
