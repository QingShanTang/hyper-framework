package org.qingshan.pojo.web;

import lombok.Data;

import java.io.Serializable;

@Data
public class MockCallContext implements Serializable {
    private Object data;
    private Boolean ifError = false;
    private Boolean ifThrow = false;
}
