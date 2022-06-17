package org.qingshan.utils.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DbUtil {

    /**
     * 测试数据库是否可以正常连接
     * 测试数据库连接
     *
     * @param dbURL
     * @param username
     * @param password
     * @return
     */
    public static boolean checkConn(String dbURL, String username, String password) {
        try (Connection dbConn = DriverManager.getConnection(dbURL, username, password)) {
            return true;
        } catch (SQLException e) {
            log.error("数据库连接失败,errorMsg->", e);
            return false;
        }
    }
}
