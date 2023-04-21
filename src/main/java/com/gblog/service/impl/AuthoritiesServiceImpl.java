package com.gblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gblog.dao.AuthoritiesDao;
import com.gblog.po.Authorities;
import com.gblog.service.AuthoritiesService;
import org.springframework.stereotype.Service;

/**
* @author GanQu
* @description 针对表【authorities】的数据库操作Service实现
* @createDate 2023-04-20 18:35:15
*/
@Service
public class AuthoritiesServiceImpl extends ServiceImpl<AuthoritiesDao, Authorities>
        implements AuthoritiesService {

}




