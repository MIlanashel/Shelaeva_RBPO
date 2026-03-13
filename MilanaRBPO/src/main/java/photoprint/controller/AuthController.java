package photoprint.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import photoprint.dto.*;
import photoprint.model.entity.Role;
import photoprint.model.entity.User;
import photoprint.repository.UserRepository;
import photoprint.auth.TokenPairService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenPairService tokenPairService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          TokenPairService tokenPairService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenPairService = tokenPairService;
    }

    // ✅ Регистрация
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid UserRegistrationDto dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Пользователь с таким именем уже существует");
        }

        User user = new User(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                Role.USER
        );

        userRepository.save(user);

        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    // ✅ Логин → access + refresh
    @PostMapping("/login")
    public TokenPairResponse login(@RequestBody LoginRequest req) {
        return tokenPairService.login(req.getUsername(), req.getPassword());
    }

    // ✅ Обновление токенов
    @PostMapping("/refresh")
    public TokenPairResponse refresh(@RequestBody RefreshRequest req) {
        return tokenPairService.refresh(req.getRefreshToken());
    }
}
