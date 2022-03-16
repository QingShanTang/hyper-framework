package org.qingshan.utils.validate;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 参数校验结果
 */
@Data
public class ValidateResult {
    /**
     * 校验结果
     */
    private boolean ifSuccess = false;

    /**
     * 校验信息
     */
    private List errorMsgList;

    public ValidateResult(boolean ifSuccess, List errorMsgList) {
        this.ifSuccess = ifSuccess;
        this.errorMsgList = errorMsgList;
    }

    public String getErrorMsg() {
        StringBuffer errorMsg = new StringBuffer();
        if (CollectionUtils.isNotEmpty(errorMsgList)) {
            errorMsg.append("数据校验异常");
            errorMsg.append(" , ");
            errorMsg.append("errorMsg->");
            errorMsg.append(errorMsgList.stream().collect(Collectors.joining(" , ")));
        }
        return errorMsg.toString();
    }
}
