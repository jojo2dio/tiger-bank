package org.zoo.common.result;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Result<T> {
    private int code;
    private T data;
    private String message;
    private boolean success;
    private LocalDateTime timestamp;
    private String traceId;

    public Result() {
        this.timestamp = LocalDateTime.now();
        this.traceId = UUID.randomUUID().toString();
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
}
