package ir.azki.error;

import lombok.Getter;

@Getter
public enum ApplicationError {
    USER_NOT_FOUND("user.not_found"),
    INVALID_CREDENTIALS("invalid.credentials"),
    RESOURCE_NOT_FOUND("resource.not_found"),
    SLOT_NOT_AVAILABLE("slot.not_available"),
    SERVER_ERROR("server.error");

    private String code;

    ApplicationError(String code) {
        this.code = code;
    }
}
