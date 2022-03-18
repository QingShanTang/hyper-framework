package org.qingshan.utils.feign;

import org.junit.Test;
import org.qingshan.pojo.web.MockCallContext;
import org.qingshan.pojo.web.MockCallResult;
import org.qingshan.utils.json.JSONUtil;

public class TestFeign {

    @Test
    public void testCall() {
        FeignProperties properties = new FeignProperties();
        properties.setConfigPath("feign.properties");
        FeignServiceFactory factory = new FeignServiceFactory(properties);
        MockService mockService = factory.buildService("hyper", MockService.class, null);
        MockCallContext context = new MockCallContext();
        context.setData("hello web");
        MockCallResult result = mockService.call(context);
        JSONUtil.printJSONStringWithFormat(result);
    }
}
