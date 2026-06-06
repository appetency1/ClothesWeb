package org.example.clothesweb.dao;

import org.example.clothesweb.entity.Order;
import org.example.clothesweb.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> findByUserId(Long userId, Integer status) {
        StringBuilder sql = new StringBuilder("SELECT o.*, u.username FROM t_order o LEFT JOIN t_user u ON o.user_id = u.id WHERE o.user_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (status != null) {
            sql.append(" AND o.status = ?");
            params.add(status);
        }
        sql.append(" ORDER BY o.create_time DESC");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Order> list = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return list;
    }

    public List<Order> findAll(Integer status, String username) {
        StringBuilder sql = new StringBuilder("SELECT o.*, u.username FROM t_order o LEFT JOIN t_user u ON o.user_id = u.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND o.status = ?");
            params.add(status);
        }
        if (username != null && !username.isEmpty()) {
            sql.append(" AND u.username LIKE ?");
            params.add("%" + username + "%");
        }
        sql.append(" ORDER BY o.create_time DESC");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Order> list = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return list;
    }

    public Order findById(Long id) {
        String sql = "SELECT o.*, u.username FROM t_order o LEFT JOIN t_user u ON o.user_id = u.id WHERE o.id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Order order = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                order = extractOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return order;
    }

    public boolean insert(Order order) {
        String sql = "INSERT INTO t_order (order_no, user_id, clothes_details, total_amount, address, status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, order.getOrderNo());
            pstmt.setLong(2, order.getUserId());
            pstmt.setString(3, order.getClothesDetails());
            pstmt.setBigDecimal(4, order.getTotalAmount());
            pstmt.setString(5, order.getAddress());
            pstmt.setInt(6, order.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean updateStatus(Long id, Integer status) {
        String sql = "UPDATE t_order SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setLong(2, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM t_order WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    private Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderNo(rs.getString("order_no"));
        order.setUserId(rs.getLong("user_id"));
        order.setClothesDetails(rs.getString("clothes_details"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setAddress(rs.getString("address"));
        order.setStatus(rs.getInt("status"));
        order.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        order.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        order.setUsername(rs.getString("username"));
        return order;
    }
}
