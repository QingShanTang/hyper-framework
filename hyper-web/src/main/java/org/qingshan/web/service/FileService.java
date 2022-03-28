package org.qingshan.web.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.qingshan.utils.file.FilePojo;
import org.qingshan.utils.file.minio.MinioUtil;
import org.qingshan.web.pojo.FileUploadParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileService {

    @Autowired
    private MinioUtil minioUtil;

    @SneakyThrows
    public void upload(FileUploadParams params) {
        log.info("备注:{}", params.getRemark());
        for (MultipartFile item : params.getFileList()) {
            FilePojo file = new FilePojo(item.getOriginalFilename(), item.getSize(), item.getInputStream());
            minioUtil.putObject("haha", "wowo.json", file.getIn());
        }
    }
}
