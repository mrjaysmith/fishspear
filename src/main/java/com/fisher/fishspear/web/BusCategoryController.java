package com.fisher.fishspear.web;


import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.response.ResponseData;
import com.fisher.fishspear.common.response.ResponseTableData;
import com.fisher.fishspear.service.IBusCategoryService;
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
 * @since 2019-05-23
 */
@RestController
@RequestMapping("/category")
public class BusCategoryController {

    @Resource
    private IBusCategoryService categoryService;

    /**
     * 返回插件分类列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(Integer limit, Integer current, String name) {
        Page page = new Page<>();
        page.setSize(limit).setCurrent(current);
        page = categoryService.list(name, page);
        return new ResponseTableData<>(page.getRecords()).setCount(page.getTotal());
    }

    /**
     * 删除插件分类
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseData delBatch(Integer categoryId) {
        categoryService.deleteById(categoryId);
        return new ResponseData<>();
    }
}

