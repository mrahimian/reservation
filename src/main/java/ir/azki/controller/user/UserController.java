package ir.azki.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.azki.controller.BaseController;
import ir.azki.controller.user.models.AuthRequest;
import ir.azki.controller.user.models.AuthResponse;
import ir.azki.service.user.UserApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "APIs for user authentication and management")
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    /**
     * POST /api/users/authenticate
     * Authenticate user and return JWT
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        var token = userApplicationService.authenticate(request.getUsername(), request.getPassword());
        return success(new AuthResponse(token));
    }
}
