package com.gblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gblog.dao.AuthoritiesDao;
import com.gblog.dao.UserDao;
import com.gblog.po.Authorities;
import com.gblog.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023/4/20 15:40
 */
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthoritiesDao authoritiesDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        QueryWrapper<User> userQW = new QueryWrapper<>();
        userQW.eq("username", username);
        User user = userDao.selectOne(userQW);
        if (user == null) {
            throw new UsernameNotFoundException("找不到该用户: " + username);
        }

        QueryWrapper<Authorities> authQW = new QueryWrapper<>();
        authQW.eq("username", username);
        Authorities auth = authoritiesDao.selectOne(authQW);

        if (auth == null) {
            log.info("找不到该用户的权限: " + username+"，使用默认权限");
            auth = new Authorities();
            auth.setUsername(username);
            auth.setAuthority("ROLE_USER");
            authoritiesDao.insert(auth);
            log.info("已为该用户添加默认权限: " + username);
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "ROLE_USER";
                }
            });
        }else {
            log.info("已找到该用户的权限: " + username);
            Authorities finalAuth = auth;
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return finalAuth.getAuthority();
                }
            });
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
