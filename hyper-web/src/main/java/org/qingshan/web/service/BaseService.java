package org.qingshan.web.service;

import com.github.pagehelper.PageHelper;
import org.qingshan.web.pojo.BasePagingParams;

public class BaseService {
    protected void prePage(BasePagingParams pageInfo) {
        Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        if (null != pageNum && null != pageSize) {
            PageHelper.startPage(pageNum, pageSize);
        }
    }
}
