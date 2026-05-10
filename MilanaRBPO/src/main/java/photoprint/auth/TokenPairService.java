package photoprint.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import photoprint.model.entity.*;
import photoprint.repository.*;
import photoprint.dto.*;

@Service
public class TokenPairService {

    private final JwtTokenProvider jwt;
    private final UserRepository userRepo;
    private final UserSessionRepository sessionRepo;
    private final PasswordEncoder encoder;

    public TokenPairService(JwtTokenProvider jwt,
                            UserRepository userRepo,
                            UserSessionRepository sessionRepo,
                            PasswordEncoder encoder) {
        this.jwt = jwt;
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.encoder = encoder;
    }

    // ЛОГИН (пароль проверяем)
    public TokenPairResponse login(String username, String rawPassword) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        return issueTokenPair(user);
    }

    public TokenPairResponse refresh(String refreshToken) {
        var claims = jwt.parse(refreshToken).getBody();

        if (!"refresh".equals(claims.get("type"))) {
            throw new RuntimeException("Wrong token type");
        }

        Long sessionId = claims.get("sessionId", Long.class);

        UserSession oldSession = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (oldSession.getStatus() != SessionStatus.ACTIVE) {
            throw new RuntimeException("Session revoked");
        }

        oldSession.setStatus(SessionStatus.REVOKED);
        sessionRepo.save(oldSession);

        return issueTokenPair(oldSession.getUser());
    }

    private TokenPairResponse issueTokenPair(User user) {
        // Создаём сессию для пользователя
        UserSession session = sessionRepo.save(
                new UserSession(
                        user,
                        "TEMP",
                        jwt.getRefreshExpiry()
                )
        );

        // Генерация refresh токена
        String refresh = jwt.generateRefreshToken(user, session.getId());

        // Хэшируем refresh токен через SHA-256 вместо BCrypt
        session.setRefreshTokenHash(sha256(refresh));
        sessionRepo.save(session);

        // Генерация access токена
        String access = jwt.generateAccessToken(user, session.getId());

        // Возвращаем оба токена
        return new TokenPairResponse(access, refresh);
    }

    // Метод SHA-256 для хэширования refresh токена
    private String sha256(String input) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
