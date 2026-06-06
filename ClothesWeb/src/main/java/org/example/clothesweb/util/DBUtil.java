package org.example.clothesweb.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static HikariDataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            if (is == null) {
                throw new RuntimeException("找不到db.properties配置文件");
            }
            props.load(is);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName(props.getProperty("db.driver"));

            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximum-pool-size")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimum-idle")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connection-timeout")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idle-timeout")));
            config.setMaxLifetime(Long.parseLong(props.getProperty("hikari.max-lifetime")));

            dataSource = new HikariDataSource(config);
            logger.info("数据库连接池初始化成功");
        } catch (IOException e) {
            logger.error("数据库连接池初始化失败", e);
            throw new RuntimeException("数据库连接池初始化失败", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("关闭ResultSet失败", e);
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("关闭PreparedStatement失败", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("关闭Connection失败", e);
            }
        }
    }

    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("数据库连接池已关闭");
        }
    }
}
