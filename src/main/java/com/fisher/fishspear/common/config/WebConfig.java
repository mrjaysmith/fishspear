package com.fisher.fishspear.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${server.file-upload-path}")
    private String filePath;

    /**
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true);
    }

    /**
     * 静态路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //在默认"/**"静态文件映射上添加"file: filePath/"
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/", "/","file:" + filePath + File.separator);
    }

    /**
     * 文件上传临时目录
     * @return
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String tmpPath = filePath + File.separator + "temp";
        File file = new File(tmpPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        factory.setLocation(tmpPath);
        return factory.createMultipartConfig();
    }
}
