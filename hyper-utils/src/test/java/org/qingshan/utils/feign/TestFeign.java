package org.qingshan.utils.feign;

import org.junit.Before;
import org.junit.Test;
import org.qingshan.pojo.web.MockCallContext;

public class TestFeign {
    private FeignServiceFactory feignServiceFactory;

    @Before
    public void before() {
        FeignProperties configProperties = new FeignProperties();
        configProperties.setConfigPath("feign.properties");
        feignServiceFactory = new FeignServiceFactory(configProperties);
    }

    @Test
    public void testFeign() {
        WebService webService = feignServiceFactory.buildService("hyper", WebService.class, new ServiceDefaultFallback());
        webService.call(new MockCallContext());
    }
}
