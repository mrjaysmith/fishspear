package com.fisher.fishspear.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.BusAddons;
import com.fisher.fishspear.mapper.BusAddonsMapper;
import com.fisher.fishspear.service.IBusAddonsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-23
 */
@Service
public class BusAddonsServiceImpl extends ServiceImpl<BusAddonsMapper, BusAddons> implements IBusAddonsService {

    @Override
    public Page list(String name, Page page) {
        return page.setRecords(baseMapper.list(name, page));
    }
}
