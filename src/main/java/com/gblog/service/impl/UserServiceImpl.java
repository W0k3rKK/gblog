package com.gblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gblog.dao.UserDao;
import com.gblog.po.User;
import com.gblog.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-03-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User>
        implements UserService {

}
