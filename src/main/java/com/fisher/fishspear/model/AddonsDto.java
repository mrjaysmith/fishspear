package com.fisher.fishspear.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于向fishspear-server发送上传插件请求
 */
@Data
@Accessors(chain = true)
public class AddonsDto {

//    private MultipartFile file;
    private String downloadUrl;
    private String name;
    private String owner;
    private String info;
    private String version;
    private String categoryIds;
    private String addonsVersion;
}
