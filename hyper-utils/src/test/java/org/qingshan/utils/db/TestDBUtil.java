package org.qingshan.utils.db;

import org.junit.Test;

public class TestDBUtil {
    @Test
    public void TestCheckConn() {
        DbUtil.checkConn("jdbc:mysql://127.0.0.1:3306/hyperpulse1", "root", "123456");
    }
}
