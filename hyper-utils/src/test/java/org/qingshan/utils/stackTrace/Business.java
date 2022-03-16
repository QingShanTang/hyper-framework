package org.qingshan.utils.stackTrace;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Business {
    public void entrance() {
//方法链路追踪代码演示
        /**
         * 层级嵌套代码
         */
        oneFloor();

        /**
         * loop
         * traceTree上会标注循环次数
         */
        for (int i = 0; i < 5; i++) {
            loopMethod();
        }

        /**
         * public方法
         * public方法可以被动态代理，【如果】系统开启代理模式，那么不需要显式编写链路追踪代码
         */
        publicMethod();

        /**
         * private方法
         * private方法不可以被动态代理，所以【必须】显式编写链路追踪代码
         */
        privateMethod();

        /**
         * 代码块链路追踪
         * 【推荐】
         */
        codeBlock1();
        /**
         * 代码块链路追踪
         */
        codeBlock2();

        /**
         * 断点链路追踪
         */
        breakPoint();

        /**
         * 以上所有方法名传参类型为String，可以是中文英文，但是推荐使用英文名，参考方法命名规范。
         */
        chineseMethodName();
    }

    @SneakyThrows
    public void loopMethod() {
        log.info("进入loopMethod...");
        Thread.sleep(2);
    }

    @SneakyThrows
    public void publicMethod() {
        log.info("进入publicMethod");
        Thread.sleep(3);
    }

    private void privateMethod() {
        try (StackTracer.Block block = StackTracer.Block.trace("privateMethod")) {
            log.info("逻辑代码");
            Thread.sleep(3);
        } catch (Exception e) {
            log.error("异常了");
        }
    }

    /**
     * 推荐
     */
    private void codeBlock1() {
        log.info("逻辑代码1");
        try (StackTracer.Block block = StackTracer.Block.trace("codeBlock1")) {
            log.info("逻辑代码2");
            Thread.sleep(3);
        } catch (Exception e) {
            log.error("异常了");
        }
        log.info("逻辑代码3");
    }

    @SneakyThrows
    private void codeBlock2() {
        log.info("逻辑代码1");
        StackTracer.Block.enter("codeBlock2");
        log.info("逻辑代码2");
        Thread.sleep(3);
        StackTracer.Block.exit();
        log.info("逻辑代码3");
    }

    @SneakyThrows
    public void oneFloor() {
        log.info("一层");
        Thread.sleep(5);
        secondFloor();
    }

    public void secondFloor() {
        log.info("二层");
        threeFloor();
        for (int i = 0; i < 2; i++) {
            loopMethod();
        }
    }

    private void breakPoint() {
        StackTracer.Point.record("breakPoint");
    }

    private void chineseMethodName() {
        StackTracer.Point.record("中文方法名");
    }

    @SneakyThrows
    public void threeFloor() {
        log.info("三层");
        Thread.sleep(10);
    }
}
