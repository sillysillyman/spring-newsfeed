package io.sillysillyman.springnewsfeed.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sillysillyman.springnewsfeed.common.ValidatorUtils;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class SignupRequestDtoTest {

    @Test
    public void should_CreateSignupRequestDto_when_ValidDataProvided() {
        //given
        String username = "validUser123";
        String password = "ValidPassword123!";
        String email = "valid@example.com";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        assertNotNull(signupRequestDto);
        assertEquals(username, signupRequestDto.getUsername());
        assertEquals(password, signupRequestDto.getPassword());
        assertEquals(email, signupRequestDto.getEmail());
    }

    @Test
    public void should_FailValidation_when_UsernameIsBlank() {
        // given
        String username = "";
        String password = "ValidPassword123!";
        String email = "valid@example.com";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "username", "사용자 ID는 필수 입력 값입니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("사용자 ID는 필수 입력 값입니다.", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_UsernameIsTooShort() {
        // given
        String username = "short";
        String password = "ValidPassword123!";
        String email = "valid@example.com";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "username", "사용자 ID는 최소 10글자 이상, 최대 20글자 이하여야 합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("사용자 ID는 최소 10글자 이상, 최대 20글자 이하여야 합니다.", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_UsernameHasInvalidCharacters() {
        // given
        String username = "invalid_user!";
        String password = "ValidPassword123!";
        String email = "valid@example.com";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "username", "사용자 ID는 대소문자 포함 영문 + 숫자만을 허용합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("사용자 ID는 대소문자 포함 영문 + 숫자만을 허용합니다.", violation.getMessage());
        assertEquals("username", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_PasswordIsBlank() {
        // given
        String username = "validUser123";
        String password = "";
        String email = "valid@example.com";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "password", "비밀번호는 필수 입력 값입니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("비밀번호는 필수 입력 값입니다.", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_PasswordIsTooShort() {
        // given
        String username = "validUser123";
        String password = "short1!";
        String email = "valid@example.com";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "password", "비밀번호는 최소 10글자 이상이어야 합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("비밀번호는 최소 10글자 이상이어야 합니다.", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_PasswordLacksSpecialCharacter() {
        // given
        String username = "validUser123";
        String password = "ValidPassword123";
        String email = "valid@example.com";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "password", "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.", violation.getMessage());
        assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_EmailIsBlank() {
        // given
        String username = "validUser123";
        String password = "ValidPassword123!";
        String email = "";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "email", "이메일은 필수 입력 값입니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("이메일은 필수 입력 값입니다.", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_EmailIsInvalid() {
        // given
        String username = "validUser123";
        String password = "ValidPassword123!";
        String email = "invalidEmail";

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // then
        Set<ConstraintViolation<SignupRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            signupRequestDto, "email", "올바른 이메일 형식이어야 합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("올바른 이메일 형식이어야 합니다.", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
    }
}