package ir.azki.service.user;

import ir.azki.auth.JwtProvider;
import ir.azki.data.user.UserRepository;
import ir.azki.error.exceptions.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static ir.azki.error.ApplicationError.USER_NOT_FOUND;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserApplicationService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public String authenticate(String username, String rawPassword) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Invalid username or password:" + username, USER_NOT_FOUND));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new NotFoundException("Invalid username or password:" + username, USER_NOT_FOUND);
        }

        return jwtProvider.generateToken(user.getId(), user.getUsername());
    }
}
