package com.fisher.fishspear.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysUser;
import com.fisher.fishspear.mapper.SysUserMapper;
import com.fisher.fishspear.service.ISysUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public Page list(String username, Page page) {
        List<SysUser> list = this.baseMapper.list(username, page);
        return page.setRecords(list);
    }
}
