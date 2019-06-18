package com.fisher.fishspear.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysUser;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
public interface ISysUserService extends IService<SysUser> {

    Page list(String username, Page page);

}
