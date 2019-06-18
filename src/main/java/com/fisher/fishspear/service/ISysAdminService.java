package com.fisher.fishspear.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysAdmin;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
public interface ISysAdminService extends IService<SysAdmin> {

    /**
     * 后台查询管理员列表
     * @param page
     * @return
     */
    Page list(String username, Page page);

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    boolean add(SysAdmin admin);

    /**
     * 编辑管理员
     * @param admin
     * @return
     */
    boolean edit(SysAdmin admin);

}
