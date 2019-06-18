package com.fisher.fishspear.common.utils;

import com.fisher.fishspear.common.shiro.ShiroUser;
import org.apache.shiro.SecurityUtils;

import java.util.Set;

public class ShiroUtil {

    public static boolean isPermitted(String menuCode) {
        return SecurityUtils.getSubject().isPermitted(menuCode);
    }

    public static ShiroUser getShiroUser() {
        return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取用户权限
     * @return
     */
    public static Set<String> getRoles() {
        return getShiroUser().getRoles();
    }

    /**
     * 是否包含管理员权限
     * @return
     */
    public static boolean isAdmin() {
        return getShiroUser().getRoles().contains("admin");
    }
}
