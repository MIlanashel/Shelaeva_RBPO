package photoprint.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import photoprint.model.entity.User;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final Key key =
            Keys.hmacShaKeyFor("SUPER_SECRET_KEY_32_BYTES_MINIMUM_12345!".getBytes());

    private final long ACCESS_TTL = 5 * 60 * 1000;      // 5 мин
    private final long REFRESH_TTL = 7 * 24 * 60 * 60 * 1000; // 7 дней

    public String generateAccessToken(User user, Long sessionId) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of(
                        "type", "access",
                        "role", user.getRole().name(),
                        "sessionId", sessionId
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TTL))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(User user, Long sessionId) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of(
                        "type", "refresh",
                        "sessionId", sessionId
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TTL))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public Instant getRefreshExpiry() {
        return Instant.now().plusMillis(REFRESH_TTL);
    }
}
