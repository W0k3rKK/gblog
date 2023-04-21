package com.gblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gblog.po.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-03-23
 */
@Repository
public interface UserDao extends BaseMapper<User> {

}
