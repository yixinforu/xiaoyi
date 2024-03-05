package com.yixin.xiaoyi.common.exception.session;

/**
 * 会话异常
 *
 * @author Tait
 */
public class SessionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SessionException() {
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
