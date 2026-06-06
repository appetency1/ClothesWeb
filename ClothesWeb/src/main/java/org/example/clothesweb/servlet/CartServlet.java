package org.example.clothesweb.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothesweb.entity.Cart;
import org.example.clothesweb.service.CartService;
import org.example.clothesweb.util.JsonResult;
import org.example.clothesweb.util.TokenUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/cart/*")
public class CartServlet extends HttpServlet {
    private CartService cartService = new CartService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        try {
            String token = req.getHeader("token");
            Long userId = TokenUtil.getUserId(token);

            if ("/list".equals(pathInfo)) {
                List<Cart> list = cartService.getCartList(userId);
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

            if ("/add".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                Long clothesId = ((Double) params.get("clothesId")).longValue();
                String clothesSize = (String) params.get("clothesSize");
                Integer quantity = ((Double) params.get("quantity")).intValue();

                cartService.addToCart(userId, clothesId, clothesSize, quantity);
                resp.getWriter().write(gson.toJson(JsonResult.success("添加成功", null)));
            } else if ("/update".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                Long cartId = ((Double) params.get("cartId")).longValue();
                Integer quantity = ((Double) params.get("quantity")).intValue();

                cartService.updateCartQuantity(cartId, quantity);
                resp.getWriter().write(gson.toJson(JsonResult.success("更新成功", null)));
            } else if ("/delete".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                Long cartId = ((Double) params.get("cartId")).longValue();
                cartService.removeFromCart(cartId);
                resp.getWriter().write(gson.toJson(JsonResult.success("删除成功", null)));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }
}
