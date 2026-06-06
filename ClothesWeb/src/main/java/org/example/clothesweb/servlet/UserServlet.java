package org.example.clothesweb.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothesweb.entity.User;
import org.example.clothesweb.service.UserService;
import org.example.clothesweb.util.JsonResult;
import org.example.clothesweb.util.TokenUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        try {
            if ("/register".equals(pathInfo)) {
                handleRegister(req, resp);
            } else if ("/login".equals(pathInfo)) {
                handleLogin(req, resp);
            } else if ("/update".equals(pathInfo)) {
                handleUpdate(req, resp);
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        try {
            if ("/info".equals(pathInfo)) {
                handleGetInfo(req, resp);
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        Map<String, String> params = gson.fromJson(json.toString(), HashMap.class);
        String username = params.get("username");
        String password = params.get("password");
        String phone = params.get("phone");
        String address = params.get("address");

        User user = userService.register(username, password, phone, address);
        user.setPassword(null);

        String token = TokenUtil.generateToken(user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        resp.getWriter().write(gson.toJson(JsonResult.success("注册成功", result)));
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        Map<String, String> params = gson.fromJson(json.toString(), HashMap.class);
        String username = params.get("username");
        String password = params.get("password");

        User user = userService.login(username, password);
        user.setPassword(null);

        String token = TokenUtil.generateToken(user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        resp.getWriter().write(gson.toJson(JsonResult.success("登录成功", result)));
    }

    private void handleGetInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("token");
        Long userId = TokenUtil.getUserId(token);

        User user = userService.getUserInfo(userId);
        user.setPassword(null);

        resp.getWriter().write(gson.toJson(JsonResult.success(user)));
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("token");
        Long userId = TokenUtil.getUserId(token);

        BufferedReader reader = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        User user = gson.fromJson(json.toString(), User.class);
        user.setId(userId);

        userService.updateUser(user);

        User updatedUser = userService.getUserInfo(userId);
        updatedUser.setPassword(null);

        resp.getWriter().write(gson.toJson(JsonResult.success("更新成功", updatedUser)));
    }
}
