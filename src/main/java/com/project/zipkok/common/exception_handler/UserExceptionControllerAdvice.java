package com.project.zipkok.common.exception_handler;

import com.project.zipkok.common.exception.BadRequestException;
import com.project.zipkok.common.exception.oauth.NoExistUser;
import com.project.zipkok.common.response.BaseExceptionResponse;
import com.project.zipkok.common.response.BaseResponse;
import com.project.zipkok.dto.oauth.GetOauthSignUpResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.URL_NOT_FOUND;

@Slf4j
@RestControllerAdvice
@Priority(0)
public class UserExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoExistUser.class)
    public BaseResponse handle_NoExistUser(NoExistUser e) {
        log.error("[handle_BadRequest]", e);
        return new BaseResponse(e.getExceptionStatus(), new GetOauthSignUpResponse(e.getEmail()));
    }
}

