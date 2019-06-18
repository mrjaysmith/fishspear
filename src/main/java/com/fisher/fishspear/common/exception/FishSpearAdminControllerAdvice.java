package com.fisher.fishspear.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * controller层异常处理
 */
@ControllerAdvice
@Slf4j
public class FishSpearAdminControllerAdvice {

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("test", "test");
    }

//    /**
//     * 全局自定义异常捕捉处理
//     * @param bex
//     * @return
//     */
//    @ResponseBody
//    @ExceptionHandler(value = BussinessException.class)
//    public Map errorHandler(BussinessException bex) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        log.info("Exception : " + bex.getCause() + " -- message: " + bex.getMessage() + "\n" + baos.toString());
//        //返回自定义异常信息
//        Map map = new HashMap();
//        map.put("state", bex.getCode());
//        map.put("message", bex.getMessage());
//        return map;
//    }

    /**
     * 全局系统异常捕捉
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(HttpServletRequest request , Exception ex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        log.info("Exception : " + ex.getCause() + " -- message: " + ex.getMessage() + "\n" + baos.toString());
        //返回自定义异常信息
        Map map = new HashMap();
        map.put("state", 500);
        map.put("message", ex.getMessage());
        return map;
    }
}
