package com.gblog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: W0k3rKK
 * @description jwt工具类
 * @date: 2023/3/24 17:26
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "gblog.jwt")
public class JwtUtil {

	private String secret;
	private long expire;
	private String header;

	/**
	 * 生成jwt token
	 * @param userId
	 * @return
	 */
	public String generateToken(long userId) {
		Date nowDate = new Date();
		//过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);

		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject(userId + "")
				.setIssuedAt(nowDate)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	/**
	 * 获取jwt token
	 * @param token
	 * @return
	 */
	public Claims getClaimByToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			log.debug("validate is token error ", e);
			return null;
		}
	}

	/**
	 * token是否过期
	 * @param expiration
	 * @return
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}
}
