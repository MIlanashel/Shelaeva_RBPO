package photoprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import photoprint.model.entity.UserSession;

import java.util.Optional;

public interface UserSessionRepository
        extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByRefreshTokenHash(String hash);
}
