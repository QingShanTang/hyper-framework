package org.qingshan.utils.feign;

import feign.Headers;
import feign.RequestLine;
import org.qingshan.pojo.web.MockCallContext;
import org.qingshan.pojo.web.MockCallResult;

public interface MockService {
    @RequestLine("POST /hyper/api/mock/call")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    MockCallResult call(MockCallContext context);
}
