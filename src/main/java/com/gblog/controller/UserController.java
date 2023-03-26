package com.gblog.controller;


import com.gblog.po.User;
import com.gblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") Long id) {
		return userService.getById(id);
	}


}

