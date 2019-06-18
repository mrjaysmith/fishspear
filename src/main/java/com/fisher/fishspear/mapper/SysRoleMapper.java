package com.fisher.fishspear.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fisher.fishspear.model.RoleTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    Set<String> roles(@Param("adminId") Integer adminId);

    List<SysRole> getRoles(@Param("userId") Integer userId);

    List<RoleTreeNode> selectTreeData(@Param("ids") Set<Integer> ids);

    List<SysRole> list(@Param("page") Page page);

    Integer deleteRoleMenu(@Param("roleId") Integer roleId);

    void roleMenu(@Param("roleId") Integer roleId, @Param("menuIds") List<Integer> menuIds);

    Set<Integer> getChildren(@Param("roleId") Integer roleId);

    int count(@Param("roleId") Integer roleId);

}
