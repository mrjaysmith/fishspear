package com.fisher.fishspear.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.exception.BizExceptionEnum;
import com.fisher.fishspear.common.exception.BussinessException;
import com.fisher.fishspear.common.response.ResponseData;
import com.fisher.fishspear.common.response.ResponseTableData;
import com.fisher.fishspear.common.shiro.ShiroUser;
import com.fisher.fishspear.common.utils.JwtUtil;
import com.fisher.fishspear.common.utils.ShiroUtil;
import com.fisher.fishspear.common.utils.ToolUtil;
import com.fisher.fishspear.entity.SysAdmin;
import com.fisher.fishspear.mapper.SysAdminMapper;
import com.fisher.fishspear.service.ISysAdminService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
@RestController
@RequestMapping("/admin")
public class SysAdminController {

    @Resource
    private ISysAdminService adminService;
    @Resource
    private SysAdminMapper adminMapper;

    @RequestMapping("/login")
    public ResponseData login(String username, String password) {
        if (ToolUtil.isEmpty(username) || ToolUtil.isEmpty(username)) {
            throw new BussinessException(BizExceptionEnum.USER_PASSWORD_WRONG);
        }
        SysAdmin admin = adminMapper.selectOne(new SysAdmin().setUsername(username));
        if (ToolUtil.isEmpty(admin)) {
            throw new BussinessException(BizExceptionEnum.USER_PASSWORD_WRONG);
        }
        //账号禁用
        if (admin.getDel()) {
            throw new BussinessException(BizExceptionEnum.USER_IS_FORBIDDEN);
        }
        if (ToolUtil.md5(password, admin.getSalt()).equals(admin.getPassword())) {      //shiro login被改造为token验证,此处直接对比password
            Map<String, Object> map = new HashMap<>();
            String token = JwtUtil.sign(admin);
            map.put("token", token);
            map.put("userId", admin.getId());
            map.put("username", admin.getUsername());
            return new ResponseData<>(200, "success", map);
        } else {
            throw new BussinessException(BizExceptionEnum.USER_PASSWORD_WRONG);
        }
    }

    @RequestMapping("/logout")
    public ResponseData logout() {
        ShiroUtil.logout();
        return ResponseData.DEFAULT;
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/editPassword", method = RequestMethod.GET)
    public ResponseData editPassword(String oldPassword, String newPassword) {
        ShiroUser shiroUser = ShiroUtil.getShiroUser();

        Integer id = shiroUser.getId();
        SysAdmin sysUser = adminService.selectById(id);
        String password = sysUser.getPassword();
        String salt = sysUser.getSalt();
        String password_old = ToolUtil.md5(oldPassword, salt);
        if (!password_old.equals(password)) {
            throw new BussinessException(BizExceptionEnum.USER_PASSWORD_WRONG);
        }
        String newSalt = ToolUtil.getRandomSalt(5);
        String password_new = ToolUtil.md5(newPassword, newSalt);

        sysUser.setPassword(password_new);
        sysUser.setSalt(newSalt);

        adminService.updateById(sysUser);
        return ResponseData.DEFAULT;
    }

    /**
     * 返回用户列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(Integer limit, Integer current, String username) {
        Page page = new Page<>();
        page.setSize(limit).setCurrent(current);
        page = adminService.list(username, page);
        return new ResponseTableData<>(page.getRecords()).setCount(page.getTotal());
    }

    /**
     * 添加修改管理员
     * @param admin
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseData edit(SysAdmin admin) {
        return new ResponseData<>(admin.getId() == null || adminService.selectById(admin.getId()) == null ?
                adminService.add(admin) : adminService.edit(admin));
    }

    /**
     * 禁用管理员
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseData delBatch(Integer adminId) {
        adminService.updateById(new SysAdmin().setId(adminId).setDel(true));
        return new ResponseData<>();
    }

    /**
     * 重置密码
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseData resetPassword(Integer adminId) {
        String randomSalt = ToolUtil.getRandomSalt(5);
        String password = ToolUtil.md5("111111", randomSalt);
        adminService.updateById(new SysAdmin().setId(adminId).setPassword(password).setSalt(randomSalt));
        return new ResponseData<>();
    }
}

