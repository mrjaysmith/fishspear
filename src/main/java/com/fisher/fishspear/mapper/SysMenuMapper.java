package com.fisher.fishspear.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysMenu;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fisher.fishspear.model.TreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuchen
 * @since 2019-05-16
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    Set<String> menus(@Param("roleId") Integer roleId);

    /**
     * 根据用户id获取权限
     */
    List<SysMenu> getMenus(@Param("userId") Integer userId);

    /**
     * 获取首页菜单列表
     * @return
     */
    List<TreeNode> selectTreeMenus(@Param("flag") Integer flag);

    /**
     * 根据角色id获取权限
     */
    List<SysMenu> getMenusByRoleId(@Param("roleId")Integer roleId);

    /**
     * 后台查询菜单列表
     * @param name
     * @param code
     * @param url
     * @param page
     * @return
     */
    List<SysMenu> selectByCondition(@Param("name") String name, @Param("code")String code,
                                    @Param("url")String url, @Param("page") Page page);

    /**
     * 获取角色所有菜单
     * @param roleId
     * @return
     */
    List<SysMenu> getAdminMenus(@Param("roleId") Integer roleId);

}
