package org.example.clothesweb.dao;

import org.example.clothesweb.entity.Clothes;
import org.example.clothesweb.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClothesDAO {

    public List<Clothes> findAll(Integer typeId, String style, String keyword, Integer status, Integer page, Integer pageSize) {
        StringBuilder sql = new StringBuilder("SELECT c.*, t.name as type_name FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (typeId != null) {
            sql.append(" AND c.type_id = ?");
            params.add(typeId);
        }
        if (style != null && !style.isEmpty()) {
            sql.append(" AND c.style = ?");
            params.add(style);
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND c.name LIKE ?");
            params.add("%" + keyword + "%");
        }
        if (status != null) {
            sql.append(" AND c.status = ?");
            params.add(status);
        }
        sql.append(" ORDER BY c.create_time DESC");

        // Add pagination
        if (page != null && pageSize != null) {
            sql.append(" LIMIT ?, ?");
            params.add((page - 1) * pageSize);
            params.add(pageSize);
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Clothes> list = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(extractClothes(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return list;
    }

    public int count(Integer typeId, String style, String keyword, Integer status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM t_clothes c WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (typeId != null) {
            sql.append(" AND c.type_id = ?");
            params.add(typeId);
        }
        if (style != null && !style.isEmpty()) {
            sql.append(" AND c.style = ?");
            params.add(style);
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND c.name LIKE ?");
            params.add("%" + keyword + "%");
        }
        if (status != null) {
            sql.append(" AND c.status = ?");
            params.add(status);
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return count;
    }

    public Clothes findById(Long id) {
        String sql = "SELECT c.*, t.name as type_name FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id WHERE c.id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Clothes clothes = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                clothes = extractClothes(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return clothes;
    }

    public boolean insert(Clothes clothes) {
        String sql = "INSERT INTO t_clothes (name, type_id, style, price, image_url, description, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clothes.getName());
            pstmt.setLong(2, clothes.getTypeId());
            pstmt.setString(3, clothes.getStyle());
            pstmt.setBigDecimal(4, clothes.getPrice());
            pstmt.setString(5, clothes.getImageUrl());
            pstmt.setString(6, clothes.getDescription());
            pstmt.setInt(7, clothes.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean update(Clothes clothes) {
        String sql = "UPDATE t_clothes SET name = ?, type_id = ?, style = ?, price = ?, image_url = ?, description = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clothes.getName());
            pstmt.setLong(2, clothes.getTypeId());
            pstmt.setString(3, clothes.getStyle());
            pstmt.setBigDecimal(4, clothes.getPrice());
            pstmt.setString(5, clothes.getImageUrl());
            pstmt.setString(6, clothes.getDescription());
            pstmt.setInt(7, clothes.getStatus());
            pstmt.setLong(8, clothes.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM t_clothes WHERE id = ?";
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

    private Clothes extractClothes(ResultSet rs) throws SQLException {
        Clothes clothes = new Clothes();
        clothes.setId(rs.getLong("id"));
        clothes.setName(rs.getString("name"));
        clothes.setTypeId(rs.getLong("type_id"));
        clothes.setStyle(rs.getString("style"));
        clothes.setPrice(rs.getBigDecimal("price"));
        clothes.setImageUrl(rs.getString("image_url"));
        clothes.setDescription(rs.getString("description"));
        clothes.setStatus(rs.getInt("status"));
        clothes.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        clothes.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        clothes.setTypeName(rs.getString("type_name"));
        return clothes;
    }
}
