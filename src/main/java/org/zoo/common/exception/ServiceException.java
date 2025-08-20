package org.zoo.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {
    private int code;
    private Throwable cause;
    private Object[] params;

    public ServiceException(String message) {
        super(message);
        this.code = 500;
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.cause = cause;
    }

    public ServiceException(int code, String message, Object[] params) {
        super(message);
        this.code = code;
        this.params = params;
    }
}
