package org.qingshan.utils.feign;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.qingshan.pojo.web.MockCallContext;
import org.qingshan.pojo.web.MockCallResult;
import org.qingshan.utils.json.JSONUtil;

@Slf4j
public class TestFeign {

    @Before
    public void before() {
        FeignProperties configProperties = new FeignProperties() {{
            setConfigPath("feign.properties");
        }};
        FeignServiceFactory.customConfig(configProperties);
    }

    @SneakyThrows
    @Test
    public void testSetListOfServers() {
        FeignServiceFactory.buildService("hyper", WebService.class, new ServiceDefaultFallback());
        MockCallResult result = ((WebService) FeignConstant.serviceContainer.get(WebService.class).getService()).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result);
        FeignDynamicConfigUtil.setListOfServers("127.0.0.1:9999");
        Thread.sleep(30000);
        MockCallResult result1 = ((WebService) FeignConstant.serviceContainer.get(WebService.class).getService()).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result1);
        FeignDynamicConfigUtil.setListOfServers("127.0.0.1:9998");
        Thread.sleep(30000);
        MockCallResult result2 = ((WebService) FeignConstant.serviceContainer.get(WebService.class).getService()).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result2);
    }

    @SneakyThrows
    @Test
    public void testSetTimeout() {
        FeignServiceFactory.buildService("hyper", WebService.class, new ServiceDefaultFallback());
        FeignDynamicConfigUtil.setTimeout(2000, 4000);
        MockCallResult result1 = ((WebService) FeignConstant.serviceContainer.get(WebService.class).getService()).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result1);
        FeignDynamicConfigUtil.setTimeout(10000, 4000);
        MockCallResult result2 = ((WebService) FeignConstant.serviceContainer.get(WebService.class).getService()).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result2);
        FeignDynamicConfigUtil.setTimeout(2000, 4000);
        MockCallResult result3 = ((WebService) FeignConstant.serviceContainer.get(WebService.class).getService()).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result3);
    }
}
