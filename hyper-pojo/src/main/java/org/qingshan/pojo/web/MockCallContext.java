package org.qingshan.pojo.web;

import lombok.Data;

@Data
public class MockCallContext {
    private Object data;
    private Boolean ifError = false;
    private Boolean ifThrow = false;
}
