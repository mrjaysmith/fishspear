package com.fisher.fishspear.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysMenu;
import com.baomidou.mybatisplus.service.IService;
import com.fisher.fishspear.model.TreeNode;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-16
 */
public interface ISysMenuService extends IService<SysMenu> {

    Set<String> getMenus(Integer userId);

    /**
     * 后台首页加载菜单列表
     * @return
     */
    List<TreeNode> treeList();

    /**
     * 后台菜单管理查询菜单列表
     * @param name
     * @param code
     * @param url
     * @param page
     * @return
     */
    Page selectByCondition(String name, String code, String url, Page page);

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    boolean add(SysMenu menu);

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    boolean edit(SysMenu menu);

    /**
     * 删除菜单
     * @param arr
     * @return
     */
    boolean updateBatch(JSONArray arr);

    /**
     * 获取菜单树,该角色的菜单默认选中
     */
    Object roleMenu(Integer roleId);
}
