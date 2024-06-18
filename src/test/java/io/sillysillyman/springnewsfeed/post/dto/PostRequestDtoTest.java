package io.sillysillyman.springnewsfeed.post.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sillysillyman.springnewsfeed.common.ValidatorUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class PostRequestDtoTest {

    private static final Validator validator = ValidatorUtils.getValidator();

    @Test
    public void should_CreatePostRequestDto_when_ValidDataProvided() {
        //given
        String title = "Test Title";
        String content = "Test Content";

        // when
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle(title);
        postRequestDto.setContent(content);
        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(postRequestDto);

        // then
        assertTrue(violations.isEmpty());
        assertNotNull(postRequestDto);
        assertEquals(title, postRequestDto.getTitle());
        assertEquals(content, postRequestDto.getContent());
    }

    @Test
    public void should_FailValidation_when_TitleIsBlank() {
        // given
        String title = "";
        String content = "Test Content";

        // when
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle(title);
        postRequestDto.setContent(content);

        // then
        Set<ConstraintViolation<PostRequestDto>> violations = ValidatorUtils.validateField(
            postRequestDto, "title");
        assertEquals(1, violations.size());

        ConstraintViolation<PostRequestDto> violation = violations.iterator().next();
        assertEquals("must not be blank", violation.getMessage());
        assertEquals("title", violation.getPropertyPath().toString());
    }

    @Test
    public void should_FailValidation_when_ContentIsNull() {
        // given
        String title = "Test Title";
        String content = null;

        // when
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle(title);
        postRequestDto.setContent(content);

        // then
        Set<ConstraintViolation<PostRequestDto>> violations = ValidatorUtils.validateField(
            postRequestDto, "content");
        assertEquals(1, violations.size());

        ConstraintViolation<PostRequestDto> violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());
        assertEquals("content", violation.getPropertyPath().toString());
    }
}