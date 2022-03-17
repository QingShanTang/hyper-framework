package org.qingshan.utils.validate;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.qingshan.utils.json.JSONUtil;

@Slf4j
public class TestValidate {

    @Test
    public void testValidateObj() {
        User user = new User();
        ValidateResult result = ValidatorUtil.validator(user);
        JSONUtil.printJSONStringWithFormat(result);
    }
}
