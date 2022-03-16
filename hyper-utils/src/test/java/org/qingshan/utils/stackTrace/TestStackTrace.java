package org.qingshan.utils.stackTrace;


import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;

@Slf4j
public class TestStackTrace {
    @Test
    public void testTracePoint() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Business.class);
        enhancer.setCallback(new StackTraceProxy());
        Business business = (Business) enhancer.create();
        try (StackTracer stackTracer = StackTracer.start("start", () -> {
            StackTracer.printStackTree();
        })) {
            business.entrance();
        } catch (Exception e) {
        }
    }
}
