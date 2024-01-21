package com.project.zipkok.common.exception.oauth;

import com.project.zipkok.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class NoExistUser extends RuntimeException{

    private final ResponseStatus exceptionStatus;
    private String email;

    public NoExistUser(ResponseStatus exceptionStatus, String email) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
        this.email = email;
    }
}
