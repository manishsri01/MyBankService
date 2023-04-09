package org.my.bank.exception;

import org.springframework.http.HttpStatusCode;

public class CustomException extends Throwable {
    private final String message;
    private final HttpStatusCode status;
    public CustomException(String message, HttpStatusCode status) {
        super();
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
