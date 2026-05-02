package com.joaquinogallar.personalblog.post.exception;

import com.joaquinogallar.personalblog.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends ResourceNotFoundException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
