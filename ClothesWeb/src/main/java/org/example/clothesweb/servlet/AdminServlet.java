package org.example.clothesweb.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothesweb.entity.Clothes;
import org.example.clothesweb.entity.Order;
import org.example.clothesweb.service.ClothesService;
import org.example.clothesweb.service.OrderService;
import org.example.clothesweb.util.JsonResult;
import org.example.clothesweb.util.TokenUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/*")
public class AdminServlet extends HttpServlet {
    private ClothesService clothesService = new ClothesService();
    private OrderService orderService = new OrderService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        try {
            String token = req.getHeader("token");
            Long userId = TokenUtil.getUserId(token);

            if ("/clothes/list".equals(pathInfo)) {
                String typeIdStr = req.getParameter("typeId");
                String style = req.getParameter("style");
                String keyword = req.getParameter("keyword");
                String statusStr = req.getParameter("status");
                String pageStr = req.getParameter("page");
                String pageSizeStr = req.getParameter("pageSize");

                Integer typeId = typeIdStr != null ? Integer.parseInt(typeIdStr) : null;
                Integer status = statusStr != null ? Integer.parseInt(statusStr) : null;
                Integer page = pageStr != null ? Integer.parseInt(pageStr) : 1;
                Integer pageSize = pageSizeStr != null ? Integer.parseInt(pageSizeStr) : 10;

                Map<String, Object> result = clothesService.getAllClothes(typeId, style, keyword, status, page, pageSize);
                resp.getWriter().write(gson.toJson(JsonResult.success(result)));
            } else if ("/clothes/detail".equals(pathInfo)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Clothes clothes = clothesService.getClothesDetail(id);
                resp.getWriter().write(gson.toJson(JsonResult.success(clothes)));
            } else if ("/order/list".equals(pathInfo)) {
                String statusStr = req.getParameter("status");
                String username = req.getParameter("username");
                Integer status = statusStr != null ? Integer.parseInt(statusStr) : null;

                List<Order> list = orderService.getAllOrders(status, username);
                resp.getWriter().write(gson.toJson(JsonResult.success(list)));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        try {
            String token = req.getHeader("token");
            Long userId = TokenUtil.getUserId(token);

            if ("/clothes/add".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                Clothes clothes = gson.fromJson(json.toString(), Clothes.class);
                clothesService.addClothes(clothes);
                resp.getWriter().write(gson.toJson(JsonResult.success("添加成功", null)));
            } else if ("/clothes/update".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                Clothes clothes = gson.fromJson(json.toString(), Clothes.class);
                clothesService.updateClothes(clothes);
                resp.getWriter().write(gson.toJson(JsonResult.success("更新成功", null)));
            } else if ("/clothes/delete".equals(pathInfo)) {
                Long id = Long.parseLong(req.getParameter("id"));
                clothesService.deleteClothes(id);
                resp.getWriter().write(gson.toJson(JsonResult.success("删除成功", null)));
            } else if ("/order/ship".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                Long orderId = ((Double) params.get("orderId")).longValue();
                orderService.shipOrder(orderId);
                resp.getWriter().write(gson.toJson(JsonResult.success("发货成功", null)));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }
}
