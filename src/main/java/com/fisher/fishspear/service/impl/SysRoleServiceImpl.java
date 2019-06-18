package com.fisher.fishspear.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.exception.BizExceptionEnum;
import com.fisher.fishspear.common.exception.BussinessException;
import com.fisher.fishspear.common.utils.ShiroUtil;
import com.fisher.fishspear.common.utils.ToolUtil;
import com.fisher.fishspear.entity.SysRole;
import com.fisher.fishspear.mapper.SysRoleMapper;
import com.fisher.fishspear.mapper.SysUserMapper;
import com.fisher.fishspear.model.RoleTreeNode;
import com.fisher.fishspear.model.TreeBuilder;
import com.fisher.fishspear.service.ISysRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private ISysRoleService roleService;

    @Override
    public List<RoleTreeNode> treeList() {
        Integer roleId = ShiroUtil.getShiroUser().getRoleId();
        Set<Integer> ids = getCurrentAndChildren(roleId);
        ids.add(roleId);
        return new TreeBuilder(baseMapper.selectTreeData(ids)).buildTree();
    }

    @Override
    @Transactional
    public void roleMenu(Integer roleId, String menuIds) {
        if (ToolUtil.isEmpty(menuIds)) {
            baseMapper.deleteRoleMenu(roleId);
        } else {
            String[] strs = menuIds.split(",");
            Set<Integer> ids = new HashSet<>();
            for (String s : strs) {
                ids.add(Integer.valueOf(s));
            }
            Set<Integer> allIds = getParents(ids, ids);
            if (ToolUtil.isNotEmpty(allIds)) {
                baseMapper.deleteRoleMenu(roleId);
                baseMapper.roleMenu(roleId, new ArrayList<>(allIds));
            }
        }
    }

    /**
     * 获取所有父节点
     *
     * @param res
     * @param list
     * @return
     */
    public Set<Integer> getParents(Set<Integer> res, Set<Integer> list) {
        Set<Integer> temp = getAll(list);
        if (temp.size() > 0) {
            res.addAll(temp);
            getParents(res, temp);
        }
        return res;
    }

    private Set<Integer> getAll(Set<Integer> list) {
        Set<Integer> temp = new HashSet<>();
        for (Integer id : list) {
            List<Integer> t = baseMapper.selectList(new EntityWrapper<SysRole>().eq("id", id))
                    .stream().filter(s -> s.getPid() != null).map(SysRole::getPid).collect(Collectors.toList());
            if (t != null && t.size() > 0) {
                temp.addAll(t);
            }
        }
        return temp;
    }

    /**
     * 获取角色所有父节点
     *
     * @param res
     * @param list
     * @return
     */
    public Set<Integer> getRoleParents(Set<Integer> res, Set<Integer> list) {
        Set<Integer> temp = getAll(list);
        if (temp.size() > 0) {
            res.addAll(temp);
            getRoleParents(res, temp);
        }
        return res;
    }

    /**
     * 获取角色所有子节点
     *
     * @param res
     * @param list
     * @return
     */
    public Set<Integer> getRoleChildren(Set<Integer> res, Set<Integer> list) {
        Set<Integer> temp = new HashSet<>();
        for (Integer id : list) {
            List<Integer> t = baseMapper.selectList(new EntityWrapper<SysRole>().eq("pid", id))
                    .stream().map(SysRole::getId).collect(Collectors.toList());
            if (t != null && t.size() > 0) {
                temp.addAll(t);
            }
        }
        if (temp.size() > 0) {
            res.addAll(temp);
            getRoleChildren(res, temp);
        }
        return res;
    }

    /**
     * 获取当前角色及所有下级角色
     * @param roleId
     * @return
     */
    private Set<Integer> getCurrentAndChildren(Integer roleId) {
        Set<Integer> ids;
        Set<Integer> idSet = new HashSet<>();
        idSet.add(roleId);
        ids = roleService.getRoleChildren(new HashSet<>(), idSet);
        ids.add(roleId);
        return ids;
    }

    @Override
    public Page list(Page page) {
        return page.setRecords(this.baseMapper.list(page));
    }

    /**
     * 删除角色
     * @param list
     * @return
     */
    @Override
    @Transactional
    public boolean del(List<Integer> list) {
        //是否存在子角色
        Set<Integer> ids = getRoleChildren(new HashSet<>(), new HashSet(list));
        if (ToolUtil.isNotEmpty(ids)) {
            throw new BussinessException(BizExceptionEnum.ROLE_HAS_CHILDREN);
        }
        int n = baseMapper.count(list.get(0));
        //管理用户，不能删除
        if (n != 0) {
            throw new BussinessException(BizExceptionEnum.ROLE_IS_USED);
        }
        //删除角色
        return baseMapper.deleteById(list.get(0)) == 1;
    }

}
