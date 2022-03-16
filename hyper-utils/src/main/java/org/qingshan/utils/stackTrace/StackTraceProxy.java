package org.qingshan.utils.stackTrace;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 动态代理
 */
public class StackTraceProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try (StackTracer.Method target = StackTracer.Method.trace(method.getName())) {
            return methodProxy.invokeSuper(o, objects);
        }
    }
}
