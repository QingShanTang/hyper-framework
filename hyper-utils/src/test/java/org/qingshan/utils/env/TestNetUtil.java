package org.qingshan.utils.env;

import lombok.SneakyThrows;
import org.junit.Test;

public class TestNetUtil {

    @SneakyThrows
    @Test
    public void testGetMacs() {
        System.out.println(NetUtil.macs);
        System.out.println(NetUtil.hostname);
        System.out.println(NetUtil.macs);
        System.out.println(NetUtil.hostname);

    }
}
