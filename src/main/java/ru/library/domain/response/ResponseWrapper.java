package ru.library.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseWrapper {

    @NonNull
    private Status status;

    private Object data;

    public static ResponseWrapper data(Object data) {
        ResponseWrapper responseWrapper = new ResponseWrapper(
                Status.NO_ERRORS, data
        );
        return responseWrapper;
    }

    public static ResponseWrapper error(ErrorCode errorCode) {
        Status status = new Status(
                errorCode.getStatusCode().getCode(),
                errorCode.getErrorCode(),
                errorCode.getErrorPhrase(),
                ""
        );
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatus(status);
        return responseWrapper;

    }

    private ResponseWrapper() {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Status {

        @NonNull
        private Integer code;
        private Integer errorCode;
        private String error;
        private String warning;

        public static final Status NO_ERRORS = new Status(
                StatusCode.NO_ERRORS.getCode()
        );
    }

    public enum StatusCode {
        NO_ERRORS(0),
        USER_ERRORS(1),
        NO_RESPONSE_DATA(2),
        UNATHORIZED(3),
        ACCESS_DENIED(4),
        NOT_AVAILABLE(5)
        ;

        private int code;

        StatusCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
