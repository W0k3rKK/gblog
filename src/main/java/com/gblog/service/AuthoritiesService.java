package com.gblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gblog.po.Authorities;

import java.util.Set;

/**
 * @author: W0k3rKK
 * @description 服务类
 * @date: 2023/4/20 18:48
 */
public interface AuthoritiesService extends IService<Authorities> {

    Set<String> getAuthorityByName(String username);
}
