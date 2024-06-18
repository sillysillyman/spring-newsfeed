package io.sillysillyman.springnewsfeed.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class EmailVerificationTest {

    @Test
    public void should_HaveCorrectFields_when_EmailVerificationIsCreated() {
        // given
        User user = new User();
        String code = "testCode";

        // when
        EmailVerification emailVerification = new EmailVerification(user, code);

        // then
        assertEquals(user, emailVerification.getUser());
        assertEquals(code, emailVerification.getCode());
        assertNotNull(emailVerification.getExpiryDate());

        LocalDateTime expectedExpiryDate = LocalDateTime.now().plusMinutes(3);
        Duration duration = Duration.between(expectedExpiryDate, emailVerification.getExpiryDate());
        assertTrue(duration.toMinutes() < 1,
            "Expiry date should be within 1 minute of the expected expiry date"); // 만료 기한 오차 1분 허용
    }

    @Test
    public void should_HaveDefaultValues_when_EmailVerificationIsCreatedWithDefaultConstructor() {
        // given: No specific given clause needed

        // when
        EmailVerification emailVerification = new EmailVerification();

        // then
        assertNotNull(emailVerification);
        assertNotNull(emailVerification.getCreatedAt());
        assertNotNull(emailVerification.getUpdatedAt());
    }
}