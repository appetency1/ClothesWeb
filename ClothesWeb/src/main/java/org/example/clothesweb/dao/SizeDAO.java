package org.example.clothesweb.dao;

import org.example.clothesweb.entity.Size;
import org.example.clothesweb.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SizeDAO {

    public List<Size> findByTypeId(Long typeId) {
        String sql = "SELECT * FROM t_size WHERE type_id = ? ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Size> list = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, typeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(extractSize(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return list;
    }

    private Size extractSize(ResultSet rs) throws SQLException {
        Size size = new Size();
        size.setId(rs.getLong("id"));
        size.setTypeId(rs.getLong("type_id"));
        size.setName(rs.getString("name"));
        size.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        return size;
    }
}
