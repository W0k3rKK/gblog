package com.gblog.springsecurity;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWTUtil;
import com.gblog.common.MyConstant;
import com.gblog.service.impl.CustomUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author: W0k3rKK
 * @description jwt过滤器，拦截请求，校验token
 *              <p>继承 OncePerRequestFilter，其会拦截http请求，然后检查其header: Authorization携带的 jwt。
 *              如果通过了就从jwt中获取用户名，然后到数据库（或者redis）里查询用户信息，然后生成验证通过的UsernamePasswordAuthenticationToken 。
 *              一定要注意这次使用的是3个参数的构造函数，其将认证状态设置为了true。</p>
 *
 * @date: 2023/4/7 11:27
 */
@Slf4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

	private final static String AUTH_HEADER = "Authorization";
	private final static String AUTH_HEADER_TYPE = "Bearer";

	@Autowired
	private CustomUserDetailService userDetailsService;

	@Autowired
	private JwtAuthProvider jwtAuthProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//		获取请求头中的token: Authorization: Bearer <authToken>
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String requestBody = sb.toString();
		JSONObject jsonObject = new JSONObject(requestBody);
		String username = jsonObject.getStr("username");
		String password = jsonObject.getStr("password");
		log.info("username:{}, password:{}", username, password);


		String authHeader = jsonObject.getStr(AUTH_HEADER);

		if (StringUtils.isEmpty(authHeader)) {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
			Authentication authentication = jwtAuthProvider.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
			return;
		}
		if (Objects.isNull(authHeader) || !authHeader.startsWith(AUTH_HEADER_TYPE)) {
			filterChain.doFilter(request, response);
			return;
		}

//		截取token，去掉Bearer以及空格
		String authToken = authHeader.substring(AUTH_HEADER_TYPE.length()).trim();
		log.info("doFilterInternal(),authToken:{}", authToken);

//		校验token
		if (!JWTUtil.verify(authToken, MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))) {
			log.info("doFilterInternal(),token校验失败");
			filterChain.doFilter(request, response);
			return;
		}

//		校验成功，获取用户名
		username = (String) JWTUtil.parseToken(authToken).getPayload("username");
		log.info("doFilterInternal(),username:{}", username);
//		根据用户名获取用户信息
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//		构建UsernamePasswordAuthenticationToken,并设置为已认证
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


//		将UsernamePasswordAuthenticationToken设置到SecurityContext上下文中
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
