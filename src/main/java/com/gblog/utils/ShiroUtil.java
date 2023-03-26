package com.gblog.utils;

import com.gblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @author: W0k3rKK
 * @description TODO
 * @date: 2023-3-26 13:25:14
 */
public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
