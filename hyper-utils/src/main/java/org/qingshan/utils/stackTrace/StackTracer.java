package org.qingshan.utils.stackTrace;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StackTracer implements AutoCloseable {
    static ThreadLocal<Runnable> endCallbackTL = new ThreadLocal<Runnable>();

    @Override
    public void close() throws Exception {
        end();
    }

    protected static StackTracer start(String stackMethod) {
        return start(stackMethod, null);
    }

    protected static StackTracer start(String stackMethod, Runnable endCallback) {
        StackTree.start(stackMethod);
        endCallbackTL.set(endCallback);
        return new StackTracer();
    }

    protected static void end() {
        StackTree.exit();
        if (null != endCallbackTL.get()) {
            endCallbackTL.get().run();
            endCallbackTL.remove();
        }
        StackTree.clear();
    }

    public static void printStackTree() {
        log.info("\n" + StackTree.getCurrentTree().toString());
    }

    protected static class Method implements AutoCloseable {
        @Override
        public void close() throws Exception {
            StackTree.exit();
        }

        public static Method trace(String stackMethod) {
            StackTree.enter(stackMethod, StackTraceEnum.targetType.METHOD);
            return new Method();
        }
    }

    public static class Block implements AutoCloseable {
        @Override
        public void close() throws Exception {
            StackTree.exit();
        }

        public static Block trace(String stackMethod) {
            StackTree.enter(stackMethod, StackTraceEnum.targetType.BLOCK);
            return new Block();
        }

        public static void enter(String stackMethod) {
            StackTree.enter(stackMethod, StackTraceEnum.targetType.BLOCK);
        }

        public static void exit() {
            StackTree.exit();
        }
    }

    public static class Point {
        public static void record(String stackMethod) {
            StackTree.enter(stackMethod, StackTraceEnum.targetType.POINT);
            StackTree.exit();
        }
    }
}
