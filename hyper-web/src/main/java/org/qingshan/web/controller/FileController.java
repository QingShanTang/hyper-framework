package org.qingshan.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.qingshan.utils.json.JSONUtil;
import org.qingshan.web.pojo.FileUploadParams;
import org.qingshan.web.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 不可以使用@RequestBody
     *
     * @param params
     */
    @PostMapping("/upload")
    public void upload(@Validated FileUploadParams params) {
        log.info("Input params->params:{}", JSONUtil.toJSONString(params));
        fileService.upload(params);
    }
}
