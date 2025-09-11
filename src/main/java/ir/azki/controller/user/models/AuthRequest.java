package ir.azki.controller.user.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {
    @NotNull(message = "username.required")
    private String username;
    @NotNull(message = "password.required")
    private String password;
}
