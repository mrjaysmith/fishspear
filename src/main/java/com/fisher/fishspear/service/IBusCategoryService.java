package com.fisher.fishspear.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.BusCategory;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-23
 */
public interface IBusCategoryService extends IService<BusCategory> {

    /**
     * 后台查询插件分类列表
     * @param page
     * @return
     */
    Page list(String name, Page page);

}
