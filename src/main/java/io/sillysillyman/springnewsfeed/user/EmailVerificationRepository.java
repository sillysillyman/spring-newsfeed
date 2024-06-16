package io.sillysillyman.springnewsfeed.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByCode(String code);

    Optional<EmailVerification> findByUser(User user);
}
