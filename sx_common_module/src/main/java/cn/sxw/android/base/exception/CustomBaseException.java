package cn.sxw.android.base.exception;

/**
 * 自定义异常
 * <p>
 * 系统中所有异常都转化为Exceptions
 */
public class CustomBaseException extends RuntimeException {
    public static final String CAUSE_REFRESH_TOKEN_TIMEOUT = "CAUSE_REFRESH_TOKEN_TIMEOUT";
    public static final String CAUSE_TOKEN_TIMEOUT = "CAUSE_TOKEN_TIMEOUT";

    public CustomBaseException() {
        super();
    }

    public CustomBaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CustomBaseException(String detailMessage) {
        super(detailMessage);
    }

    public CustomBaseException(Throwable throwable) {
        super(throwable);
    }

}
