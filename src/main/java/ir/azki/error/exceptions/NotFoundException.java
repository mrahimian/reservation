package ir.azki.error.exceptions;

import ir.azki.error.ApplicationError;

public class NotFoundException extends BaseException {

    public NotFoundException(String message, ApplicationError err) {
        super(message, err);
    }
}
