package com.joaquinogallar.personalblog.tag.exception;

import com.joaquinogallar.personalblog.exception.ResourceNotFoundException;

public class TagNotFoundException extends ResourceNotFoundException {
    public TagNotFoundException(String message) {
        super(message);
    }
}
