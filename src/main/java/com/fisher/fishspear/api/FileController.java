package com.fisher.fishspear.api;

import com.fisher.fishspear.common.exception.BizExceptionEnum;
import com.fisher.fishspear.common.exception.BussinessException;
import com.fisher.fishspear.common.response.ResponseData;
import com.fisher.fishspear.common.utils.FileUtil;
import com.fisher.fishspear.common.utils.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

@Controller
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Value("${server.file-upload-path}")
    private String filePath;

    /**
     * 上传文件到服务器本地
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData upload(MultipartFile file) {
        if (ToolUtil.isEmpty(file)) {
            throw new BussinessException(BizExceptionEnum.UPLOAD_ERROR);
        }
        File newFile;
        String fileName = file.getOriginalFilename();
        try {
            newFile = new File(filePath + File.separator + fileName);
            if (newFile.exists()) {
                newFile = new File(filePath + File.separator + new Date().getTime() + "_" + fileName);
            }
            fileName = newFile.getName();
            file.transferTo(newFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BussinessException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return new ResponseData<>(fileName);
    }

    /**
     * 获取服务器本地文件
     * @param fileName
     * @param response
     */
    @RequestMapping("/download")
    public void download(String fileName, HttpServletResponse response) {
        try {
            byte[] bytes = FileUtil.toByteArray(filePath + File.separator + fileName);
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            throw new BussinessException(BizExceptionEnum.FILE_NOT_FOUND);
        }
    }
}
