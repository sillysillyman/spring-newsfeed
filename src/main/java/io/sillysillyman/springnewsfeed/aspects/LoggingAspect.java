package io.sillysillyman.springnewsfeed.aspects;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(io.sillysillyman.springnewsfeed..*)")
    public void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    public void logBeforeAllMethods(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        
        log.info("Request URL: {}", request.getRequestURL());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("Method: {}", joinPoint.getSignature().getName());
    }
}
