package com.fisher.fishspear.web;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.exception.BizExceptionEnum;
import com.fisher.fishspear.common.exception.BussinessException;
import com.fisher.fishspear.common.response.ResponseData;
import com.fisher.fishspear.common.response.ResponseTableData;
import com.fisher.fishspear.common.utils.ToolUtil;
import com.fisher.fishspear.entity.SysRole;
import com.fisher.fishspear.mapper.SysRoleMapper;
import com.fisher.fishspear.service.ISysRoleService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private ISysRoleService sysRoleService;

    @RequestMapping("/list")
    public ResponseData list(Integer limit, Integer current) {
        Page page = new Page<>(current,limit);
        page = sysRoleService.list(page);
        return new ResponseTableData<>(page.getRecords()).setCount(page.getTotal());
    }

    @RequestMapping("/add")
    public ResponseData add(SysRole role) {
        SysRole parent = sysRoleMapper.selectById(role.getPid());
        if (ToolUtil.isEmpty(parent)) {
            throw new BussinessException(BizExceptionEnum.CHOICE_PARENT_ROLE);
        }
        if (role.getId() == null) {         //添加
            sysRoleMapper.insert(role);
        } else {                            //修改
            sysRoleMapper.updateById(role);
        }
        return new ResponseData();
    }

    @RequestMapping("/del")
    public ResponseData del(String del) {
        List<Integer> ids = JSONArray.parseArray(del, Integer.class);
        sysRoleService.del(ids);
        return new ResponseData();
    }

    /**
     * 分配权限
     */
    @RequestMapping("/rolemenu")
    public ResponseData roleMenu(Integer roleId, String menuIds) {
        sysRoleService.roleMenu(roleId, menuIds);
        return new ResponseData();
    }

    /**
     * 返回树型角色集合
     */
    @RequestMapping("/treeData")
    public Object treeData() {
        return sysRoleService.treeList();
    }

}

