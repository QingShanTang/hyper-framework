package org.qingshan.web.controller.pojo;

import lombok.Data;

@Data
public class MockContext {
    private Object data;
    private Boolean ifError = false;
    private Boolean ifThrow = false;
}
