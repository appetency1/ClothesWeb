package org.example.clothesweb.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothesweb.entity.Order;
import org.example.clothesweb.service.CartService;
import org.example.clothesweb.service.OrderService;
import org.example.clothesweb.util.JsonResult;
import org.example.clothesweb.util.TokenUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/order/*")
public class OrderServlet extends HttpServlet {
    private OrderService orderService = new OrderService();
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
                String statusStr = req.getParameter("status");
                Integer status = statusStr != null ? Integer.parseInt(statusStr) : null;

                List<Order> list = orderService.getUserOrders(userId, status);
                resp.getWriter().write(gson.toJson(JsonResult.success(list)));
            } else if ("/detail".equals(pathInfo)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Order order = orderService.getOrderDetail(id);
                resp.getWriter().write(gson.toJson(JsonResult.success(order)));
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

            if ("/create".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                String clothesDetails = (String) params.get("clothesDetails");
                BigDecimal totalAmount = new BigDecimal(params.get("totalAmount").toString());
                String address = (String) params.get("address");

                Order order = orderService.createOrder(userId, clothesDetails, totalAmount, address);
                cartService.clearCart(userId);

                resp.getWriter().write(gson.toJson(JsonResult.success("创建订单成功", order)));
            } else if ("/pay".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                Long orderId = ((Double) params.get("orderId")).longValue();
                orderService.payOrder(orderId);
                resp.getWriter().write(gson.toJson(JsonResult.success("支付成功", null)));
            } else if ("/confirm".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                Long orderId = ((Double) params.get("orderId")).longValue();
                orderService.confirmReceive(orderId);
                resp.getWriter().write(gson.toJson(JsonResult.success("确认收货成功", null)));
            } else if ("/delete".equals(pathInfo)) {
                BufferedReader reader = req.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                Map<String, Object> params = gson.fromJson(json.toString(), HashMap.class);
                Long orderId = ((Double) params.get("orderId")).longValue();
                orderService.deleteOrder(orderId);
                resp.getWriter().write(gson.toJson(JsonResult.success("删除成功", null)));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }
}
