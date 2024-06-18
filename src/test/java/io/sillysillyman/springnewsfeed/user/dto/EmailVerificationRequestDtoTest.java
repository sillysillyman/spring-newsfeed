package io.sillysillyman.springnewsfeed.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sillysillyman.springnewsfeed.common.ValidatorUtils;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmailVerificationRequestDtoTest {

    @Test
    public void should_CreateEmailVerificationRequestDto_when_ValidDataProvided() {
        // given
        String code = "123456";

        // when
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto();
        emailVerificationRequestDto.setCode(code);

        // then
        Set<ConstraintViolation<EmailVerificationRequestDto>> violations = ValidatorUtils.validateField(
            emailVerificationRequestDto, "code");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void should_FailValidation_when_CodeIsBlank() {
        // given
        String code = "";

        // when
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto();
        emailVerificationRequestDto.setCode(code);

        // then
        Set<ConstraintViolation<EmailVerificationRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            emailVerificationRequestDto, "code", "인증 코드를 입력해주세요.");

        // for (var violation : violations) {
        //     System.out.println("Violation: " + violation.getMessage());
        // }

        assertEquals(1, violations.size());

        ConstraintViolation<EmailVerificationRequestDto> violation = violations.iterator().next();
        assertEquals("인증 코드를 입력해주세요.", violation.getMessage());
        assertEquals("code", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_CodeIsNotSixDigits() {
        // given
        String code = "12345";

        // when
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto();
        emailVerificationRequestDto.setCode(code);

        // then
        Set<ConstraintViolation<EmailVerificationRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            emailVerificationRequestDto, "code", "인증 코드는 6자리 숫자이어야 합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<EmailVerificationRequestDto> violation = violations.iterator().next();
        assertEquals("인증 코드는 6자리 숫자이어야 합니다.", violation.getMessage());
        assertEquals("code", violation.getPropertyPath().toString());
    }
}