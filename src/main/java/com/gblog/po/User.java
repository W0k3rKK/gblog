package com.gblog.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-03-23
 */
@Data
@TableName("m_user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@NotBlank(message = "昵称不能为空")
	private String username;

	private String avatar;

	@Email(message = "邮箱格式不正确")
	@NotBlank(message = "邮箱不能为空")
	private String email;

	private String password;

	private String created;

	private String lastLogin;

	@NotBlank(message = "可用性不能为空")
	private int enabled;

}
