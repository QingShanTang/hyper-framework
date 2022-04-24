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
        FeignServiceFactory.buildService("hyper", WebService.class, new ServiceDefaultFallback());
    }

    @SneakyThrows
    @Test
    public void testSetListOfServers() {
        MockCallResult result = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result);
        FeignDynamicConfigUtil.setListOfServers("127.0.0.2:9999");
        MockCallResult result1 = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result1);
        FeignDynamicConfigUtil.setListOfServers("127.0.0.3:9999");
        MockCallResult result2 = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result2);
        FeignDynamicConfigUtil.setListOfServers("127.0.0.1:9999");
        MockCallResult result3 = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result3);
        FeignDynamicConfigUtil.setListOfServers("127.0.0.3:9999");
        MockCallResult result4 = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result4);
    }

    @SneakyThrows
    @Test
    public void testSetTimeout() {
        FeignServiceFactory.buildService("hyper", WebService.class, new ServiceDefaultFallback());
        FeignDynamicConfigUtil.setTimeout(2000, 4000);
        MockCallResult result1 = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result1);
        FeignDynamicConfigUtil.setTimeout(10000, 4000);
        MockCallResult result2 = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result2);
        FeignDynamicConfigUtil.setTimeout(2000, 4000);
        MockCallResult result3 = FeignUtil.getService(WebService.class).call(new MockCallContext());
        JSONUtil.printJSONStringWithFormat(result3);
    }
}
