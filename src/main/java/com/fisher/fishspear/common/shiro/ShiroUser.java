package com.fisher.fishspear.common.shiro;

import com.fisher.fishspear.entity.SysAdmin;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class ShiroUser extends SysAdmin {

    private Set<String> roles;
    private Set<String> menus;

    public ShiroUser(SysAdmin admin) {
        setId(admin.getId());
        setRoleId(admin.getRoleId());
        setAvatar(admin.getAvatar());
        setUsername(admin.getUsername());
        setPassword(admin.getPassword());
        setPhone(admin.getPhone());
        setSalt(admin.getSalt());
        setDel(admin.getDel());
        roles = new HashSet<>();
        menus = new HashSet<>();
    }
}
