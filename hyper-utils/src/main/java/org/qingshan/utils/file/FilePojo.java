package org.qingshan.utils.file;

import lombok.Data;

import java.io.InputStream;

/**
 * 文件对象，用来MultipartFile与File互转
 */
@Data
public class FilePojo {
    private String name;
    private Long size;
    private InputStream in;

    public FilePojo(String name, Long size, InputStream in) {
        this.name = name;
        this.size = size;
        this.in = in;
    }
}
