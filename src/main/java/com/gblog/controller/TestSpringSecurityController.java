package com.gblog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023/4/7 10:21
 */
@RestController
@RequestMapping("/auth")
public class TestSpringSecurityController {

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
}
