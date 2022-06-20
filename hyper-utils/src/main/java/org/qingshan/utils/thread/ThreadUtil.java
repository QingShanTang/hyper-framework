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
        log.info("Interrupte thread,threadId:{}", id);
        Thread thread = ThreadUtils.findThreadById(id);
        if (null != thread) {
            thread.interrupt();
            log.info("Interrupted thread succeeded");
        } else {
            log.warn("This thread does not exist,threadId:{}", id);
        }
    }
}
