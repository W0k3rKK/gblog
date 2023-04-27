package com.gblog.common.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023/4/27 17:27
 */
public class CustomUserDetailsUser extends User implements Serializable {

    private Long userId;

    public CustomUserDetailsUser(Long userId,String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }
}
