package org.example.clothesweb.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clothesweb.entity.Size;
import org.example.clothesweb.service.SizeService;
import org.example.clothesweb.util.JsonResult;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/size/*")
public class SizeServlet extends HttpServlet {
    private SizeService sizeService = new SizeService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json;charset=UTF-8");

        try {
            if ("/list".equals(pathInfo)) {
                String typeIdStr = req.getParameter("typeId");
                Long typeId = Long.parseLong(typeIdStr);
                List<Size> list = sizeService.getSizesByTypeId(typeId);
                resp.getWriter().write(gson.toJson(JsonResult.success(list)));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(JsonResult.error(e.getMessage())));
        }
    }
}
