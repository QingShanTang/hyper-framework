package org.qingshan.web.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.qingshan.web.pojo.FileUploadParams;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@Service
public class FileService {
    @SneakyThrows
    public void upload(FileUploadParams params) {
        log.info("备注:{}", params.getRemark());
        for (MultipartFile item : params.getFileList()) {
            File file = new File(item.getOriginalFilename());
            item.transferTo(file);
            System.out.println("xixi");
        }
    }
}
