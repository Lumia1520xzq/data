package com.wf.uic.common.exception;

public class SmsServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SmsServiceException() {
        super();
    }

    public SmsServiceException(String message) {
        super(message);
    }

    public SmsServiceException(Throwable cause) {
        super(cause);
    }

    public SmsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
