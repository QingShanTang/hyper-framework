package org.qingshan.utils.feign;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.qingshan.pojo.web.MockCallContext;
import org.qingshan.pojo.web.MockCallResult;

@Slf4j
public class ServiceDefaultFallback implements FallbackFactory<WebService> {
    @Override
    public WebService create(Throwable throwable) {
        return new WebService() {
            @Override
            public MockCallResult call(MockCallContext params) {
                log.error("远程调用失败,errorMsg->{}", throwable);
                MockCallResult result = new MockCallResult();
                result.setResult(false);
                result.setData("hystrix降级");
                return result;
            }
        };
    }
}
