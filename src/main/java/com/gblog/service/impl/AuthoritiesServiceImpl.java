package com.gblog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gblog.dao.AuthoritiesDao;
import com.gblog.po.Authorities;
import com.gblog.service.AuthoritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author GanQu
 * @description 针对表【authorities】的数据库操作Service实现
 * @createDate 2023-04-20 18:35:15
 */
@Service
public class AuthoritiesServiceImpl extends ServiceImpl<AuthoritiesDao, Authorities>
        implements AuthoritiesService {

    @Autowired
    private AuthoritiesDao authoritiesDao;

    @Override
    public Set<String> getAuthorityByName(String username) {
        List<Authorities> list = authoritiesDao.selectList(Wrappers.<Authorities>query().lambda().eq(Authorities::getUsername, username));
//        使用lambda遍历获取权限,并放到Set<String>里
        return list.stream().map(Authorities::getAuthority).collect(Collectors.toSet());

    }
}




