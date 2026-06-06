package org.example.clothesweb.service;

import org.example.clothesweb.dao.ClothesDAO;
import org.example.clothesweb.entity.Clothes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothesService {
    private ClothesDAO clothesDAO = new ClothesDAO();

    public Map<String, Object> getClothesList(Integer typeId, String style, String keyword, Integer page, Integer pageSize) {
        List<Clothes> list = clothesDAO.findAll(typeId, style, keyword, 1, page, pageSize);
        int total = clothesDAO.count(typeId, style, keyword, 1);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }

    public Clothes getClothesDetail(Long id) {
        return clothesDAO.findById(id);
    }

    public Map<String, Object> getAllClothes(Integer typeId, String style, String keyword, Integer status, Integer page, Integer pageSize) {
        List<Clothes> list = clothesDAO.findAll(typeId, style, keyword, status, page, pageSize);
        int total = clothesDAO.count(typeId, style, keyword, status);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }

    public boolean addClothes(Clothes clothes) {
        return clothesDAO.insert(clothes);
    }

    public boolean updateClothes(Clothes clothes) {
        return clothesDAO.update(clothes);
    }

    public boolean deleteClothes(Long id) {
        return clothesDAO.delete(id);
    }
}
