package org.example.clothesweb.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothesweb.entity.Clothes;
import org.example.clothesweb.service.ClothesService;
import org.example.clothesweb.util.JsonResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/clothes/*")
public class ClothesServlet extends HttpServlet {
    private ClothesService clothesService = new ClothesService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        try {
            if ("/list".equals(pathInfo)) {
                String typeIdStr = req.getParameter("typeId");
                String style = req.getParameter("style");
                String keyword = req.getParameter("keyword");
                String pageStr = req.getParameter("page");
                String pageSizeStr = req.getParameter("pageSize");

                Integer typeId = typeIdStr != null ? Integer.parseInt(typeIdStr) : null;
                Integer page = pageStr != null ? Integer.parseInt(pageStr) : 1;
                Integer pageSize = pageSizeStr != null ? Integer.parseInt(pageSizeStr) : 12;

                Map<String, Object> result = clothesService.getClothesList(typeId, style, keyword, page, pageSize);
                resp.getWriter().write(gson.toJson(JsonResult.success(result)));
            } else if ("/detail".equals(pathInfo)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Clothes clothes = clothesService.getClothesDetail(id);
                resp.getWriter().write(gson.toJson(JsonResult.success(clothes)));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }
}
