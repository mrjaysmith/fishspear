package com.fisher.fishspear.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.exception.BizExceptionEnum;
import com.fisher.fishspear.common.exception.BussinessException;
import com.fisher.fishspear.common.shiro.ShiroUser;
import com.fisher.fishspear.common.utils.ShiroUtil;
import com.fisher.fishspear.common.utils.ToolUtil;
import com.fisher.fishspear.entity.SysAdmin;
import com.fisher.fishspear.mapper.SysAdminMapper;
import com.fisher.fishspear.service.ISysAdminService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fisher.fishspear.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements ISysAdminService {

    @Resource
    private ISysRoleService roleService;

    /**
     * 后台管理查询列表
     * @param page
     * @return
     */
    @Override
    public Page list(String username, Page page) {
        return page.setRecords(baseMapper.list(username, page));
    }

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @Override
    @Transactional
    public boolean add(SysAdmin admin) {
        //查重
        SysAdmin a = baseMapper.selectOne(new SysAdmin().setUsername(admin.getUsername()));
        if (ToolUtil.isNotEmpty(a)) {
            throw new BussinessException(BizExceptionEnum.USER_IS_EXIST);
        }
        admin.setSalt(ToolUtil.getRandomSalt(5));
        admin.setPassword(ToolUtil.md5(admin.getPassword(), admin.getSalt()));
        return baseMapper.insert(admin) == 1;
    }

    /**
     * 编辑管理员
     * @param admin
     * @return
     */
    @Override
    @Transactional
    public boolean edit(SysAdmin admin) {
        //查重
        List<SysAdmin> list = baseMapper.selectList(new EntityWrapper<SysAdmin>()
                .ne("id", admin.getId())
                .eq("userName", admin.getUsername()));
        if (ToolUtil.isNotEmpty(list)) {
            throw new BussinessException(BizExceptionEnum.USER_IS_EXIST);
        }
        return baseMapper.updateById(admin) == 1;
    }

}
