package org.qingshan.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.qingshan.pojo.web.MockCallContext;
import org.qingshan.pojo.web.MockCallResult;
import org.qingshan.utils.json.JSONUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/mock")
public class MockController {

    @GetMapping("/call")
    public MockCallResult call(MockCallContext context) {
        log.info("Input params->context:{}", JSONUtil.toJSONString(context));
        MockCallResult result = new MockCallResult();
        result.setData(context.getData());
        if (context.getIfError()) {
            if (context.getIfThrow()) {
                throw new RuntimeException("自发异常!");
            }
            result.setResult(false);
        } else {
            result.setResult(true);
        }
        log.info("result:{}", JSONUtil.toJSONString(result));
        return result;
    }
}
