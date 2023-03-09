package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.example.mufiye.config.MinIOConfig;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.utils.MinIOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Api(tags = "File Controller 测试文件上传功能")
public class FileController {
    @Autowired
    private MinIOConfig minIOConfig;

    @PostMapping("upload")
    public GraceJSONResult upload(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream());
        String imageUrl = minIOConfig.getFileHost() + "/" + minIOConfig.getBucketName() + "/" + filename;
        return GraceJSONResult.ok(imageUrl);
    }
}
