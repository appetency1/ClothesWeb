package org.example.clothesweb.dao;

import org.example.clothesweb.entity.User;
import org.example.clothesweb.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public User findByUsername(String username) {
        String sql = "SELECT * FROM t_user WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = extractUser(rs);
            }
        } catch (SQLException e) {
            logger.error("数据库操作失败", e);
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return user;
    }

    public User findByPhone(String phone) {
        String sql = "SELECT * FROM t_user WHERE phone = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = extractUser(rs);
            }
        } catch (SQLException e) {
            logger.error("数据库操作失败", e);
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return user;
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM t_user WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = extractUser(rs);
            }
        } catch (SQLException e) {
            logger.error("数据库操作失败", e);
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return user;
    }

    public boolean insert(User user) {
        String sql = "INSERT INTO t_user (username, password, phone, address, role) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getRole());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean update(User user) {
        String sql = "UPDATE t_user SET username = ?, password = ?, phone = ?, address = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setLong(5, user.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setRole(rs.getInt("role"));
        user.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        user.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        return user;
    }
}
