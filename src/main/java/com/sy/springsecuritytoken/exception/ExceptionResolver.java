package com.sy.springsecuritytoken.exception;

import com.sy.springsecuritytoken.response.ErrorResponse;
import com.sy.springsecuritytoken.response.ResponseCode;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Log4j2
@RestController
public class ExceptionResolver {

    @ExceptionHandler(AuthenticationProcessingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse authenticationExceptionHandler(AuthenticationProcessingException exception) {
        printErrorLog(exception);
        return ErrorResponse.of(exception.getResponseCode());
    }

    @ExceptionHandler(value = {
        InvalidParameterException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        HandlerMethodValidationException.class,
        ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidParameterExceptionHandler(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            final String message = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s:%s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));
            printErrorLog(exception, message);
        } else {
            printErrorLog(exception);
        }
        return ErrorResponse.of(ResponseCode.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse serviceExceptionHandler(ApplicationException exception) {
        printErrorLog(exception);
        return ErrorResponse.of(exception.getResponseCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unknownError(Exception exception) {
        printErrorLog(exception);
        return ErrorResponse.of(ResponseCode.UNKNOWN);
    }

    private void printErrorLog(Exception exception, String message) {
        printErrorLog(exception);
        log.error("\n");
        log.error("=> {}", message);
    }

    private void printErrorLog(Exception exception) {
        log.error("\n");
        log.error("=> {}", exception.getMessage());
        log.error("=> {}", Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n")));
    }
}
