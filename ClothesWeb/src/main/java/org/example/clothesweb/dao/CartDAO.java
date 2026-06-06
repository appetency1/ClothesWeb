package org.example.clothesweb.dao;

import org.example.clothesweb.entity.Cart;
import org.example.clothesweb.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public List<Cart> findByUserId(Long userId) {
        String sql = "SELECT c.*, cl.name as clothes_name, cl.price as clothes_price, cl.image_url as clothes_image_url " +
                     "FROM t_cart c LEFT JOIN t_clothes cl ON c.clothes_id = cl.id " +
                     "WHERE c.user_id = ? ORDER BY c.create_time DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Cart> list = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(extractCart(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return list;
    }

    public Cart findByUserAndClothesAndSize(Long userId, Long clothesId, String clothesSize) {
        String sql = "SELECT * FROM t_cart WHERE user_id = ? AND clothes_id = ? AND clothes_size = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Cart cart = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.setLong(2, clothesId);
            pstmt.setString(3, clothesSize);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                cart = extractCart(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return cart;
    }

    public boolean insert(Cart cart) {
        String sql = "INSERT INTO t_cart (user_id, clothes_id, clothes_size, quantity) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, cart.getUserId());
            pstmt.setLong(2, cart.getClothesId());
            pstmt.setString(3, cart.getClothesSize());
            pstmt.setInt(4, cart.getQuantity());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean updateQuantity(Long id, Integer quantity) {
        String sql = "UPDATE t_cart SET quantity = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
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
        String sql = "DELETE FROM t_cart WHERE id = ?";
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

    public boolean deleteByUserId(Long userId) {
        String sql = "DELETE FROM t_cart WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    private Cart extractCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getLong("id"));
        cart.setUserId(rs.getLong("user_id"));
        cart.setClothesId(rs.getLong("clothes_id"));
        cart.setClothesSize(rs.getString("clothes_size"));
        cart.setQuantity(rs.getInt("quantity"));
        cart.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        cart.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());

        if (rs.getString("clothes_name") != null) {
            cart.setClothesName(rs.getString("clothes_name"));
            cart.setClothesPrice(rs.getBigDecimal("clothes_price"));
            cart.setClothesImageUrl(rs.getString("clothes_image_url"));
        }

        return cart;
    }
}
