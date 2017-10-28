package com.wf.uic.common.exception;

public class LoginException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

}
