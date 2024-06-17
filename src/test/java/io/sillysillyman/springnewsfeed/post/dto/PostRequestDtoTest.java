package io.sillysillyman.springnewsfeed.post.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PostRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        try (ValidatorFactory factory = Validation.byDefaultProvider().configure()
            .buildValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void should_CreatePostRequestDto_when_ValidDataProvided() {
        //given
        String title = "Test Title";
        String content = "Test Content";

        // when
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle(title);
        postRequestDto.setContent(content);

        // then
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
        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(postRequestDto);
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
        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(postRequestDto);
        assertEquals(1, violations.size());

        ConstraintViolation<PostRequestDto> violation = violations.iterator().next();
        assertEquals("must not be null", violation.getMessage());
        assertEquals("content", violation.getPropertyPath().toString());
    }
}