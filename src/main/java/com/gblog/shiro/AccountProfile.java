package com.gblog.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023/3/24 21:41
 */
@Data
public class AccountProfile implements Serializable {
	private Long id;

	private String username;

	private String avatar;

	private String email;

}
