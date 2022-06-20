package org.qingshan.utils.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ThreadUtils;

@Slf4j
public class ThreadUtil {

    /**
     * 打断线程执行
     *
     * @param id
     */
    public static void interrupteThreadById(Long id) {
        Thread thread = ThreadUtils.findThreadById(id);
        if (null != thread) {
            thread.interrupt();
        }
    }

    public static void main(String[] args) {
        interrupteThreadById(123L);
    }
}
