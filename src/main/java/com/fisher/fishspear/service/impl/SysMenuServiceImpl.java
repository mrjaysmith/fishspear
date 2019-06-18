package com.fisher.fishspear.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.exception.BizExceptionEnum;
import com.fisher.fishspear.common.exception.BussinessException;
import com.fisher.fishspear.common.shiro.ShiroUser;
import com.fisher.fishspear.common.utils.ShiroUtil;
import com.fisher.fishspear.entity.SysMenu;
import com.fisher.fishspear.mapper.SysMenuMapper;
import com.fisher.fishspear.model.TreeBuilder;
import com.fisher.fishspear.model.TreeNode;
import com.fisher.fishspear.service.ISysMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchen
 * @since 2019-05-16
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public Set<String> getMenus(Integer roleId) {
        Set<String> menus = new HashSet<>();
        List<SysMenu> list = this.baseMapper.getAdminMenus(roleId);
        for (SysMenu sysMenu : list) {
            menus.add(sysMenu.getCode());
        }
        return menus;
    }

    /**
     * 后台首页加载菜单列表
     * @return
     */
    @Override
    public List<TreeNode> treeList() {
        ShiroUser user = ShiroUtil.getShiroUser();
        if (null == user) {
            throw new BussinessException(BizExceptionEnum.USER_NOT_LOGIN);
        }
        Set<String> menus = user.getMenus();
        List<TreeNode> all = baseMapper.selectTreeMenus(null);
        List<TreeNode> nodes = new ArrayList<>();
        for (TreeNode treeNode : all) {
            if (menus.contains(treeNode.getName())) {
                nodes.add(treeNode);
            }
        }
        return new TreeBuilder(nodes).buildTree();
    }

    /**
     * 后台菜单管理查询菜单列表
     * @param name
     * @param code
     * @param url
     * @param page
     * @return
     */
    @Override
    public Page selectByCondition(String name, String code, String url, Page page) {
        return page.setRecords(baseMapper.selectByCondition(name, code, url, page));
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @Override
    @Transactional
    public boolean add(SysMenu menu) {
        Integer i = baseMapper.insert(menu);
        return i != null && i == 1 ? true : false;
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @Override
    @Transactional
    public boolean edit(SysMenu menu) {
        Integer i = baseMapper.updateById(menu);
        return i != null && i == 1 ? true : false;
    }

    /**
     * 删除菜单
     * @param arr
     * @return
     */
    @Override
    @Transactional
    public boolean updateBatch(JSONArray arr) {
        List<SysMenu> menus = new ArrayList<>();
        for (Object id : arr) {
            SysMenu menu = new SysMenu().setId(Integer.valueOf(id.toString())).setDel(true);
            menus.add(menu);
        }
        List<SysMenu> res = getChildren(menus, menus);
        return this.updateBatchById(res);
    }

    @Override
    public Object roleMenu(Integer roleId) {
        int flag = ShiroUtil.isAdmin() ? 1 : 0;
        List<TreeNode> nodes = baseMapper.selectTreeMenus(flag);
        List<SysMenu> menusByRoleId = baseMapper.getMenusByRoleId(roleId);
        for (SysMenu sysMenu : menusByRoleId) {
            for (TreeNode node : nodes) {
                if (sysMenu.getId().equals(node.getId()) && sysMenu.getPid() != null) {
                    node.setChecked(true);
                    break;
                }
            }
        }
        return new TreeBuilder(nodes).buildTree();
    }

    /**
     * 获取所有子菜单
     * @param res
     * @param list
     * @return
     */
    private List<SysMenu> getChildren(List<SysMenu> res, List<SysMenu> list) {
        List<SysMenu> temp = new ArrayList<>();
        for (SysMenu m : list) {
            List<SysMenu> t = this.baseMapper.selectList(new EntityWrapper<SysMenu>().eq("pid", m.getId()));
            if (t != null && t.size() > 0) {
                for (SysMenu menu : t) {
                    menu.setDel(true);
                }
                temp.addAll(t);
            }
        }
        if (temp.size() > 0) {
            res.addAll(temp);
            getChildren(res, temp);
        }
        return res;
    }
}
