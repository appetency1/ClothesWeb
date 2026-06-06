package org.example.clothesweb.service;

import org.example.clothesweb.dao.SizeDAO;
import org.example.clothesweb.entity.Size;

import java.util.List;

public class SizeService {
    private SizeDAO sizeDAO = new SizeDAO();

    public List<Size> getSizesByTypeId(Long typeId) {
        return sizeDAO.findByTypeId(typeId);
    }
}
