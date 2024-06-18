package io.sillysillyman.springnewsfeed.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sillysillyman.springnewsfeed.common.ValidatorUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class UpdateProfileRequestDtoTest {

    private static final Validator validator = ValidatorUtils.getValidator();

    @Test
    public void should_FailValidation_when_IntroductionIsNull() {
        // given
        String introduction = null;
        UpdateProfileRequestDto updateProfileRequestDto = new UpdateProfileRequestDto();
        updateProfileRequestDto.setIntroduction(introduction);

        // when
        Set<ConstraintViolation<UpdateProfileRequestDto>> violations = ValidatorUtils.validateField(
            updateProfileRequestDto, "introduction");

        // then
        assertEquals(1, violations.size());
        ConstraintViolation<UpdateProfileRequestDto> violation = violations.iterator().next();
        assertEquals("must not be null",
            violation.getMessage()); // Assuming default message for NotNull
        assertEquals("introduction", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_IntroductionIsTooLong() {
        // given
        String introduction =
            "This is a very long introduction that exceeds the maximum length of one hundred characters. "
                + "This should trigger a validation error.";
        UpdateProfileRequestDto updateProfileRequestDto = new UpdateProfileRequestDto();
        updateProfileRequestDto.setIntroduction(introduction);

        // when
        Set<ConstraintViolation<UpdateProfileRequestDto>> violations = ValidatorUtils.validateFieldWithMessage(
            updateProfileRequestDto, "introduction", "소개글은 최대 100글자까지 입력 가능합니다.");

        // then
        assertEquals(1, violations.size());
        ConstraintViolation<UpdateProfileRequestDto> violation = violations.iterator().next();
        assertEquals("소개글은 최대 100글자까지 입력 가능합니다.", violation.getMessage());
        assertEquals("introduction", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_PictureUrlIsInvalid() {
        // given
        String invalidUrl = "invalid-url";
        UpdateProfileRequestDto updateProfileRequestDto = new UpdateProfileRequestDto();
        updateProfileRequestDto.setPictureUrl(invalidUrl);

        // when
        Set<ConstraintViolation<UpdateProfileRequestDto>> violations = ValidatorUtils.validateField(
            updateProfileRequestDto, "pictureUrl");

        // then
        assertEquals(1, violations.size());
        ConstraintViolation<UpdateProfileRequestDto> violation = violations.iterator().next();
        assertEquals("유효한 URL 형식이어야 합니다.", violation.getMessage());
        assertEquals("pictureUrl", violation.getPropertyPath().toString());
    }

    @Test
    public void should_PassValidation_when_DataIsValid() {
        // given
        String introduction = "Short introduction within limit.";
        String pictureUrl = "http://example.com/picture.jpg";

        // when
        UpdateProfileRequestDto updateProfileRequestDto = new UpdateProfileRequestDto();
        updateProfileRequestDto.setIntroduction(introduction);
        updateProfileRequestDto.setPictureUrl(pictureUrl);

        // then
        Set<ConstraintViolation<UpdateProfileRequestDto>> violations = validator.validate(
            updateProfileRequestDto);
        assertTrue(violations.isEmpty());
        assertNotNull(updateProfileRequestDto);
        assertEquals(introduction, updateProfileRequestDto.getIntroduction());
        assertEquals(pictureUrl, updateProfileRequestDto.getPictureUrl());
    }
}