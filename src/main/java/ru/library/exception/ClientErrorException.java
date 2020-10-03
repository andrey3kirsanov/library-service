package ru.library.exception;

import lombok.Getter;
import ru.library.domain.response.ErrorCode;

@Getter
public class ClientErrorException extends Exception {
    private final ErrorCode errorCode;
    private final String description;
    private String extraMessage;

    public ClientErrorException(ErrorCode errorCode, String description) {
        super(String.format("%s(%s)", errorCode, description));
        this.errorCode = errorCode;
        this.description = description;
    }

    public ClientErrorException(ErrorCode errorCode, String description, String extraMessage) {
        super(String.format("%s(%s)", errorCode, description));
        this.errorCode = errorCode;
        this.description = description;
        this.extraMessage = extraMessage;
    }
}
