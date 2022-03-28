package org.qingshan.utils.file.minio;

import lombok.Data;

@Data
public class MinioProp {
    /**
     * 要使用9000映射的端口
     */
    private String endpoint;
    private String accesskey;
    private String secretkey;
    private String bucketName;
}
