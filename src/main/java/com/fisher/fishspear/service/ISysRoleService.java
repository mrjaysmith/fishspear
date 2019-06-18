package com.fisher.fishspear.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysRole;
import com.baomidou.mybatisplus.service.IService;
import com.fisher.fishspear.model.RoleTreeNode;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 返回角色树型结构
     * @return
     */
    List<RoleTreeNode> treeList();

    void roleMenu(Integer roleId, String menuIds);

    Set<Integer> getParents(Set<Integer> res, Set<Integer> list);

    Set<Integer> getRoleParents(Set<Integer> res, Set<Integer> list);

    Set<Integer> getRoleChildren(Set<Integer> res, Set<Integer> list);

    Page list(Page page);

    boolean del(List<Integer> asList);

}
