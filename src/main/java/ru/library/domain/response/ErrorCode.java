package ru.library.domain.response;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_ERROR(ResponseWrapper.StatusCode.NOT_AVAILABLE, 1, "Internal server error."),
    INVALID_JWT_TOKEN(ResponseWrapper.StatusCode.ACCESS_DENIED, 2, "Incorrect JWT token. Please, log in again."),
    JWT_TOKEN_EXPIRED(ResponseWrapper.StatusCode.ACCESS_DENIED, 3, "JWT token has expired. Please, log in again."),
    PASSWORD_NOT_VALID(ResponseWrapper.StatusCode.USER_ERRORS, 4, "The password should contain at least 8 maximum 32 symbols, uppercase, lowercase, digits and special symbols."),
    CREDENTIALS_ARE_INVALID(ResponseWrapper.StatusCode.USER_ERRORS, 5, "Credentials are invalid."),
    USER_NOT_FOUND(ResponseWrapper.StatusCode.USER_ERRORS, 6, "User was not found."),
    DTO_VALIDATION_FAILED(ResponseWrapper.StatusCode.USER_ERRORS, 7, "Validation failed for DTO.")
    ;

    ResponseWrapper.StatusCode statusCode;
    int errorCode;
    String errorPhrase;

    ErrorCode(ResponseWrapper.StatusCode statusCode, int errorCode, String errorPhrase) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorPhrase = errorPhrase;
    }
}
