package com.sy.springsecuritytoken.exception;

import com.sy.springsecuritytoken.error.controller.response.ErrorResponse;
import com.sy.springsecuritytoken.response.ResponseCode;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ExceptionResolver {

    @ExceptionHandler(AuthenticationProcessingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse authenticationExceptionHandler(AuthenticationProcessingException exception) {
        printErrorLog(exception);
        return ErrorResponse.of(ResponseCode.FAIL);
    }

    @ExceptionHandler({
        NotFountException.class,
        AlreadyException.class
    })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse serviceExceptionHandler(Exception exception) {
        printErrorLog(exception);
        return ErrorResponse.of(ResponseCode.FAIL);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unknownError(Exception exception) {
        printErrorLog(exception);
        return ErrorResponse.of(ResponseCode.FAIL);
    }

    private void printErrorLog(Exception exception) {
        log.error("\n");
        log.error("=> {}", exception.getMessage());
        log.error("=> {}", Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n")));
    }
}
