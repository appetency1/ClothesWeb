package org.example.clothesweb.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.clothesweb.util.JsonResult;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 1024 * 1024 * 10,  // 10MB
    maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class UploadServlet extends HttpServlet {
    private Gson gson = new Gson();
    
    // Upload directory relative to webapp
    private static final String UPLOAD_DIR = "upload";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        
        try {
            // Get upload file
            Part filePart = req.getPart("file");
            
            if (filePart == null || filePart.getSize() == 0) {
                resp.getWriter().write(gson.toJson(JsonResult.error("请选择要上传的文件")));
                return;
            }
            
            // Validate file type
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                resp.getWriter().write(gson.toJson(JsonResult.error("只能上传图片文件")));
                return;
            }
            
            // Get file extension
            String fileName = getSubmittedFileName(filePart);
            String extension = "";
            if (fileName != null && fileName.contains(".")) {
                extension = fileName.substring(fileName.lastIndexOf("."));
            }
            
            // Validate extension
            if (!extension.matches("\\.(?i)(jpg|jpeg|png|gif|webp)")) {
                resp.getWriter().write(gson.toJson(JsonResult.error("只支持 .jpg, .jpeg, .png, .gif, .webp 格式")));
                return;
            }
            
            // Generate unique filename with timestamp + UUID
            String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            
            // Get upload path (webapp/upload/)
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Save file
            String filePath = uploadPath + File.separator + newFileName;
            filePart.write(filePath);
            
            // Build file URL
            String fileUrl = "/" + UPLOAD_DIR + "/" + newFileName;
            
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", newFileName);
            
            resp.getWriter().write(gson.toJson(JsonResult.success("上传成功", result)));
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(JsonResult.error("上传失败: " + e.getMessage())));
        }
    }
    
    private String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;
        
        for (String token : header.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return null;
    }
}