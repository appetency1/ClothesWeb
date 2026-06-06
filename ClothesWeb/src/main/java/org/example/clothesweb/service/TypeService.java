package org.example.clothesweb.service;

import org.example.clothesweb.dao.TypeDAO;
import org.example.clothesweb.entity.Type;

import java.util.List;

public class TypeService {
    private TypeDAO typeDAO = new TypeDAO();

    public List<Type> getAllTypes() {
        return typeDAO.findAll();
    }
}
