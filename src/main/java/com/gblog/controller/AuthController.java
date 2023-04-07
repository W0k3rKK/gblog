package com.gblog.controller;

import cn.hutool.jwt.JWT;
import com.gblog.common.MyConstant;
import com.gblog.common.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author: W0k3rKK
 * @description 登录
 *              <p>
 *              这里的AuthenticationManager 注入的是默认实现ProviderManager实例。
 *              UsernamePasswordAuthenticationToken 是一个Authentication，
 *              我们构建一个Authentication然后交给AuthenticationManager 去校验。
 *              一定要注意这里使用的是两个参数的构造方法，它将认证状态设置为了false，
 *              接着就需要让AuthenticationManager 去校验用户名和秘密。
 *              </p>
 * @date: 2023/4/7 10:46
 */
@RestController
@RequestMapping("/user")
public class AuthController {
	//	q:无法自动装配。找不到 'AuthenticationManager' 类型的 Bean
	//	a:没有配置AuthenticationManager，需要在配置类中配置
	@Autowired
	private AuthenticationManager authenticationManager;


	@PostMapping("/login")
	public String login(@RequestBody LoginDto loginDto) {
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

		authenticationManager.authenticate(authenticationToken);

//		认证成功则颁发jwt令牌
		String token = JWT.create()
				.setPayload("username", loginDto.getUsername())
				.setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))
				.sign();


		return token;
	}

}
