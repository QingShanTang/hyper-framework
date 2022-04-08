package org.qingshan.utils.validate;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;


/**
 * 校验结果
 */
@Data
public class ValidateResult {
    /**
     * 校验结果
     */
    private boolean ifSuccess = false;

    /**
     * 异常信息
     */
    private List<String> msgs;

    public ValidateResult(boolean ifSuccess, List<String> msgs) {
        this.ifSuccess = ifSuccess;
        this.msgs = msgs;
    }

    public ValidateResult(boolean ifSuccess, String msg) {
        this.ifSuccess = ifSuccess;
        this.msgs = Arrays.asList(msg);
    }


    public String getMsgsStr() {
        return StringUtils.join(msgs, ",");
    }
}
