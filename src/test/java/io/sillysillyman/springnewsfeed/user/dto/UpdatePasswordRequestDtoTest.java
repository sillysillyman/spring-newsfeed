package io.sillysillyman.springnewsfeed.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sillysillyman.springnewsfeed.common.ValidatorUtils;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class UpdatePasswordRequestDtoTest {

    @Test
    public void should_CreateUpdatePasswordRequestDto_when_ValidDataProvided() {
        // given
        String currentPassword = "CurrentPassword123!";
        String newPassword = "NewPassword123!";

        // when
        UpdatePasswordRequestDto updatePasswordRequestDto = new UpdatePasswordRequestDto();
        updatePasswordRequestDto.setCurrentPassword(currentPassword);
        updatePasswordRequestDto.setNewPassword(newPassword);

        // then
        assertNotNull(updatePasswordRequestDto);
        assertEquals(currentPassword, updatePasswordRequestDto.getCurrentPassword());
        assertEquals(newPassword, updatePasswordRequestDto.getNewPassword());
    }

    @Test
    public void should_FailValidation_when_CurrentPasswordIsBlank() {
        // given
        String currentPassword = "";
        String newPassword = "NewPassword123!";

        // when
        UpdatePasswordRequestDto updatePasswordRequestDto = new UpdatePasswordRequestDto();
        updatePasswordRequestDto.setCurrentPassword(currentPassword);
        updatePasswordRequestDto.setNewPassword(newPassword);

        // then
        Set<ConstraintViolation<UpdatePasswordRequestDto>> violations = ValidatorUtils.validateField(
            updatePasswordRequestDto, "currentPassword");
        assertEquals(1, violations.size());

        ConstraintViolation<UpdatePasswordRequestDto> violation = violations.iterator().next();
        assertEquals("현재 비밀번호는 필수 입력 값입니다.", violation.getMessage());
        assertEquals("currentPassword", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_NewPasswordIsTooShort() {
        // given
        String currentPassword = "CurrentPassword123!";
        String newPassword = "short1!";

        // when
        UpdatePasswordRequestDto updatePasswordRequestDto = new UpdatePasswordRequestDto();
        updatePasswordRequestDto.setCurrentPassword(currentPassword);
        updatePasswordRequestDto.setNewPassword(newPassword);

        // then
        Set<ConstraintViolation<UpdatePasswordRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            updatePasswordRequestDto, "newPassword", "새 비밀번호는 최소 10글자 이상이어야 합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<UpdatePasswordRequestDto> violation = violations.iterator().next();
        assertEquals("새 비밀번호는 최소 10글자 이상이어야 합니다.", violation.getMessage());
        assertEquals("newPassword", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_NewPasswordLacksSpecialCharacter() {
        // given
        String currentPassword = "CurrentPassword123!";
        String newPassword = "NewPassword123";

        // when
        UpdatePasswordRequestDto updatePasswordRequestDto = new UpdatePasswordRequestDto();
        updatePasswordRequestDto.setCurrentPassword(currentPassword);
        updatePasswordRequestDto.setNewPassword(newPassword);

        // then
        Set<ConstraintViolation<UpdatePasswordRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            updatePasswordRequestDto, "newPassword",
            "새 비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.");
        assertEquals(1, violations.size());

        ConstraintViolation<UpdatePasswordRequestDto> violation = violations.iterator().next();
        assertEquals("새 비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.", violation.getMessage());
        assertEquals("newPassword", violation.getPropertyPath().toString());
    }
}