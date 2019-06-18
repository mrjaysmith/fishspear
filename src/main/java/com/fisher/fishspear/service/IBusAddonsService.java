package com.fisher.fishspear.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.BusAddons;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-23
 */
public interface IBusAddonsService extends IService<BusAddons> {

    /**
     * 后台查询插件列表
     * @param page
     * @return
     */
    Page list(String name, Page page);

}
