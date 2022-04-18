package org.qingshan.utils.feign;

import feign.Headers;
import feign.RequestLine;
import org.qingshan.pojo.web.MockCallContext;
import org.qingshan.pojo.web.MockCallResult;

public interface WebService {
    @RequestLine("POST /hyper/api/mock")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    MockCallResult call(MockCallContext params);
}
