package com.gblog.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gblog.common.code.Result;
import com.gblog.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023/3/23 19:06
 */
@Configuration
public class JwtFilter extends AuthenticatingFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/**
	 * 生成自定义JwtToken
	 *
	 * @param servletRequest
	 * @param servletResponse
	 * @return
	 *
	 * @throws Exception
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse)
			throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String token = request.getHeader("Authorization");
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		return new JwtToken(token);
	}

	/**
	 * 拦截所有请求,判断是否需要重新登录
	 *  头部是否携带Authorization
	 *  有: 首先校验jwt的有效性，通过则执行executeLogin方法实现自动登录
	 *  无: 直接通过
	 *
	 * @param servletRequest  the incoming <code>ServletRequest</code>
	 * @param servletResponse the outgoing <code>ServletResponse</code>
	 * @return
	 *
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String token = request.getHeader("Authorization");
		if (StringUtils.isEmpty(token)) {
			return true;
		} else {

			// 校验jwt
			Claims claim = jwtTokenUtil.getClaimByToken(token);
			if (claim == null || jwtTokenUtil.isTokenExpired(claim.getExpiration())) {
				throw new ExpiredCredentialsException("token已失效，请重新登录");
			}

			// 执行登录
			return executeLogin(servletRequest, servletResponse);
		}
	}


	/**
	 * 登录异常时
	 *
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		Throwable throwable = e.getCause() == null ? e : e.getCause();
		Result result = Result.fail(throwable.getMessage());
		String json = JSONUtil.toJsonStr(result);

		try {
			httpServletResponse.getWriter().print(json);
		} catch (IOException ioException) {

		}
		return false;
	}

	/**
	 * 跨域支持 (拦截器的前置拦截)
	 *
	 * @param servletRequest  the incoming ServletRequest
	 * @param servletResponse the outgoing ServletResponse
	 * @return
	 *
	 * @throws Exception
	 */
	@Override
	protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
		HttpServletResponse httpServletResponse = WebUtils.toHttp(servletResponse);
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个OPTIONS请求，这里给OPTIONS请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
			return false;
		}

		return super.preHandle(servletRequest, servletResponse);
	}
}
