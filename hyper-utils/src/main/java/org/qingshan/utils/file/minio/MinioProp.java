package org.qingshan.utils.file.minio;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class MinioProp {
    /**
     * 要使用9000映射的端口
     */
    private String endpoint;
    private String accesskey;
    private String secretkey;
    private List<String> buckets = new ArrayList<>();

    public void setBuckets(String buckets) {
        if (StringUtils.isNotBlank(buckets)) {
            Arrays.asList(buckets.split(",")).forEach(item -> {
                this.buckets.add(item.trim());
            });
        }
    }
}
