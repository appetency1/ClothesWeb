package org.example.clothesweb.filter;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothesweb.dao.UserDAO;
import org.example.clothesweb.entity.User;
import org.example.clothesweb.util.JsonResult;
import org.example.clothesweb.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/user/register",
            "/api/user/login",
            "/api/clothes/list",
            "/api/clothes/detail",
            "/api/type/list",
            "/api/size/list",
            "/upload"
    );

    private static final List<String> ADMIN_PATHS = Arrays.asList(
            "/api/admin"
    );

    private UserDAO userDAO = new UserDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        if (contextPath != null && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        logger.debug("请求路径: {}, 处理后路径: {}", httpRequest.getRequestURI(), path);

        for (String whitePath : WHITE_LIST) {
            if (path.startsWith(whitePath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String token = httpRequest.getHeader("token");
        if (token == null || token.isEmpty()) {
            sendError(httpResponse, "未登录，请先登录");
            return;
        }

        if (!TokenUtil.validateToken(token)) {
            sendError(httpResponse, "登录已过期，请重新登录");
            return;
        }

        Long userId = TokenUtil.getUserId(token);
        User user = userDAO.findById(userId);
        if (user == null) {
            sendError(httpResponse, "用户不存在");
            return;
        }

        for (String adminPath : ADMIN_PATHS) {
            if (path.startsWith(adminPath)) {
                if (user.getRole() != 1) {
                    logger.warn("非管理员尝试访问管理员接口: userId={}", userId);
                    sendError(httpResponse, "权限不足");
                    return;
                }
                logger.debug("管理员访问验证通过: userId={}", userId);
                break;
            }
        }

        httpRequest.setAttribute("userId", userId);
        httpRequest.setAttribute("userRole", user.getRole());
        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(JsonResult.error(message)));
    }
}
