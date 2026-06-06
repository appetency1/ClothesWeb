package org.example.clothesweb.util;

public class JsonResult<T> {
    private int code;
    private boolean success;
    private String message;
    private T data;

    public JsonResult() {
    }

    public JsonResult(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(200, true, "操作成功", data);
    }

    public static <T> JsonResult<T> success(String message, T data) {
        return new JsonResult<>(200, true, message, data);
    }

    public static <T> JsonResult<T> error(String message) {
        return new JsonResult<>(500, false, message, null);
    }

    public static <T> JsonResult<T> error(int code, String message) {
        return new JsonResult<>(code, false, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
