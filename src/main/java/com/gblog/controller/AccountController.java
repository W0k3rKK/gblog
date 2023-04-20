package com.gblog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gblog.common.code.Result;
import com.gblog.common.dto.LoginDto;
import com.gblog.po.User;
import com.gblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: W0k3rKK
 * @description 接受账号密码，把用户的id生成jwt，返回给前端，为了后续的jwt的延期，所以把jwt放在header上
 * @date: 2023/3/25 17:03
 */
@RestController
public class AccountController {
//	@Autowired
//	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;


	/**
	 * 默认账号为chris,密码为111111
	 *
	 * @param loginDto
	 * @param response
	 * @return
	 */
	@CrossOrigin
	@PostMapping("/login")
	@ResponseBody
	public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
		User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
		Assert.notNull(user, "用户不存在");

		if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
			return Result.badGateway("密码错误！");
		}
		String jwt = "";
		System.out.println("jwt = " + jwt);

		response.setHeader("Authorization", jwt);
		response.setHeader("Access-Control-Expose-Headers", "Authorization");

		return Result.succ(MapUtil.builder()
				.put("id", user.getId())
				.put("username", user.getUsername())
				.put("avatar", user.getAvatar())
				.put("email", user.getEmail())
				.map()
		);

	}

	@GetMapping("/logout")
//	@RequiresAuthentication
	public Result logout() {
//		spring security logout


		return Result.succ(null);
	}
}
