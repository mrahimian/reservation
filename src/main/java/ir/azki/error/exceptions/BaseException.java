package ir.azki.error.exceptions;

import ir.azki.error.ApplicationError;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ApplicationError error;

    public BaseException(ApplicationError err) {
        super();
        this.error = err;

    }
    public BaseException(String message, ApplicationError err) {
        super(message);
        this.error = err;
    }

}
