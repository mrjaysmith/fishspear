package com.fisher.fishspear.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.response.ResponseData;
import com.fisher.fishspear.common.response.ResponseTableData;
import com.fisher.fishspear.entity.SysMenu;
import com.fisher.fishspear.service.ISysMenuService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-05-16
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Resource
    private ISysMenuService menuService;

    /**
     * 首页菜单列表
     * @return
     */
    @RequestMapping("/treeList")
    @RequiresAuthentication
    public ResponseData treeList() {
        return new ResponseData<>(menuService.treeList());
    }

    /**
     * 菜单管理查询查单列表
     * @param name
     * @param code
     * @param url
     * @param limit
     * @param current
     * @return
     */
    @RequestMapping("/list")
    public ResponseTableData list(String name, String code, String url, Integer limit, Integer current) {
        Page page = new Page();
        page.setSize(limit).setCurrent(current);
        page = menuService.selectByCondition(name, code, url, page);
        return new ResponseTableData<>(page.getRecords()).setCount(page.getTotal());
    }

    /**
     * 删除菜单
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delBatch", method = RequestMethod.POST)
    public ResponseData delBatch(String ids) {
        JSONArray arr = JSON.parseArray(ids);
        return new ResponseData<>(menuService.updateBatch(arr));
    }

    /**
     * 添加修改菜单
     * @param menu
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseData edit(SysMenu menu) {
        return new ResponseData<>(menu.getId() == null || menuService.selectById(menu.getId()) == null ?
                menuService.add(menu) : menuService.edit(menu));
    }

    /**
     * 菜单下拉树
     * @return
     */
    @RequestMapping("/treeData")
    public Object treeData() {
        return menuService.treeList();
    }

    /**
     * 获取菜单树,该角色的菜单默认选中
     */
    @RequestMapping("/roleMenu")
    public Object roleMenu(Integer roleId) {
        return menuService.roleMenu(roleId);
    }
}

