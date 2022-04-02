package org.qingshan.web.pojo;

import lombok.Data;

@Data
public class BasePagingParams {
    //一页显示的记录
    private Integer pageSize;

    //页数
    private Integer pageNum;
}
