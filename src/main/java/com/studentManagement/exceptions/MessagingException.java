package com.studentManagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class MessagingException extends RuntimeException  {
    public MessagingException() {
        super();
    }
    public MessagingException(String message) {
        super(message);
    }
}
