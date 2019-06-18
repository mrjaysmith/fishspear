package com.fisher.fishspear.web;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.response.ResponseData;
import com.fisher.fishspear.common.response.ResponseTableData;
import com.fisher.fishspear.common.utils.ToolUtil;
import com.fisher.fishspear.entity.SysUser;
import com.fisher.fishspear.service.ISysUserService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private ISysUserService userService;

    /**
     * 返回用户列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(Integer limit, Integer current, String username) {
        Page page = new Page(current, limit);
        page = userService.list(username, page);
        return new ResponseTableData<>(page.getRecords()).setCount(page.getTotal());
    }

    /**
     * 重置密码为111111
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseData resetPassword(Integer userId) {
        String randomSalt = ToolUtil.getRandomSalt(5);
        String password = ToolUtil.md5("111111", randomSalt);
        userService.updateById(new SysUser().setId(userId).setPassword(password).setSalt(randomSalt));
        return ResponseData.DEFAULT;
    }

    /**
     * 禁用用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseData delBatch(Integer userId) {
        userService.updateById(new SysUser().setId(userId).setDel(true));
        return new ResponseData<>();
    }
}

