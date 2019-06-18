package com.fisher.fishspear.web;


import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.common.response.ResponseData;
import com.fisher.fishspear.common.response.ResponseTableData;
import com.fisher.fishspear.common.utils.HttpUtil;
import com.fisher.fishspear.entity.BusAddons;
import com.fisher.fishspear.entity.SysUser;
import com.fisher.fishspear.model.AddonsDto;
import com.fisher.fishspear.service.IBusAddonsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-05-23
 */
@Slf4j
@RestController
@RequestMapping("/addons")
public class BusAddonsController {

    @Value("${server.file-upload-path}")
    private String filePath;
    @Value("${server.file-upload-url}")
    private String uploadUrl;
    @Resource
    private IBusAddonsService addonsService;

    /**
     * 返回插件列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(Integer limit, Integer current, String name) {
        Page page = new Page<>();
        page.setSize(limit).setCurrent(current);
        page = addonsService.list(name, page);
        return new ResponseTableData<>(page.getRecords()).setCount(page.getTotal());
    }

    /**
     * 删除插件
     *
     * @param addonsId
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseData del(Integer addonsId) {
        addonsService.deleteById(addonsId);
        return new ResponseData<>();
    }

    /**
     * 添加/编辑插件
     *
     * @param addons
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseData edit(AddonsDto addons) {
        HttpResponse response = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            File file = new File(filePath + File.separator + addons.getDownloadUrl());
            HttpPost post = new HttpPost(uploadUrl);

            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
            StringBody name = new StringBody(addons.getName(), ContentType.MULTIPART_FORM_DATA);
            //todo 设置owner和info
            StringBody owner = new StringBody("root", ContentType.MULTIPART_FORM_DATA);
            StringBody info = new StringBody("info", ContentType.MULTIPART_FORM_DATA);
            StringBody version = new StringBody(addons.getVersion(), ContentType.MULTIPART_FORM_DATA);
            StringBody categoryIds = new StringBody(addons.getCategoryIds(), ContentType.MULTIPART_FORM_DATA);
            StringBody addonsVersion = new StringBody(addons.getAddonsVersion(), ContentType.MULTIPART_FORM_DATA);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file", fileBody);
            builder.addPart("name", name);
            builder.addPart("owner", owner);
            builder.addPart("info", info);
            builder.addPart("version", version);
            builder.addPart("categoryIds", categoryIds);
            builder.addPart("addonsVersion", addonsVersion);

            HttpEntity entity = builder.build();
            post.setEntity(entity);
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StatusLine statusLine = response.getStatusLine();
        log.info("addons upload result : " + statusLine.getStatusCode() + "; message : " + statusLine.getReasonPhrase());
        return new ResponseData(statusLine.getStatusCode(), statusLine.getReasonPhrase());
    }

}

