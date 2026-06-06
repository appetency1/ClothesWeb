package org.example.clothesweb.dao;

import org.example.clothesweb.entity.Type;
import org.example.clothesweb.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeDAO {

    public List<Type> findAll() {
        String sql = "SELECT * FROM t_type ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Type> list = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(extractType(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return list;
    }

    public Type findById(Long id) {
        String sql = "SELECT * FROM t_type WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Type type = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                type = extractType(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return type;
    }

    private Type extractType(ResultSet rs) throws SQLException {
        Type type = new Type();
        type.setId(rs.getLong("id"));
        type.setName(rs.getString("name"));
        type.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        return type;
    }
}
