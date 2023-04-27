package com.gblog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gblog.common.dto.CustomUserDetailsUser;
import com.gblog.po.User;
import com.gblog.service.AuthoritiesService;
import com.gblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023/4/20 15:40
 */
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getOne(Wrappers.<User>query().lambda().eq(User::getUsername, username));

        if (ObjectUtil.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getDetail(user);
    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        User user = userService.getById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getDetail(user);
    }

    private UserDetails getDetail(User user) {

        Set<String> permissions = authoritiesService.getAuthorityByName(user.getUsername());

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(CollUtil.join(permissions, ","));

        CustomUserDetailsUser customUserDetailsUser = new CustomUserDetailsUser(user.getId(),
                user.getUsername(), user.getPassword(), authorities);

        return customUserDetailsUser;
    }


}
