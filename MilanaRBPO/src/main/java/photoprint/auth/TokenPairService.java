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

    // 🔐 ЛОГИН (пароль проверяем)
    public TokenPairResponse login(String username, String rawPassword) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        return issueTokenPair(user);
    }

    // ♻️ REFRESH (БЕЗ пароля)
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

        // 🔁 Ротация refresh
        oldSession.setStatus(SessionStatus.REVOKED);
        sessionRepo.save(oldSession);

        return issueTokenPair(oldSession.getUser());
    }

    // 🧠 ЕДИНАЯ точка генерации пары токенов
    private TokenPairResponse issueTokenPair(User user) {

        UserSession session = sessionRepo.save(
                new UserSession(
                        user,
                        "TEMP",
                        jwt.getRefreshExpiry()
                )
        );

        String refresh = jwt.generateRefreshToken(user, session.getId());
        session.setRefreshTokenHash(encoder.encode(refresh));
        sessionRepo.save(session);

        String access = jwt.generateAccessToken(user, session.getId());

        return new TokenPairResponse(access, refresh);
    }
}
