package ru.library.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.library.domain.response.ErrorCode;
import ru.library.domain.response.ResponseWrapper;
import ru.library.exception.ClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class ControllersAdvice {

    @ExceptionHandler({ClientErrorException.class})
    public ResponseEntity<ResponseWrapper> clientErrorException(HttpServletRequest request, ClientErrorException e) {
        log.error("Error by {} while call {} {}: {}", e.getExtraMessage(), request.getRequestURI(), e.getErrorCode().getErrorPhrase(), e);
        return wrapException(e);
    }

    private ResponseEntity<ResponseWrapper> wrapException(ClientErrorException exception) {
        if (Objects.nonNull(exception.getExtraMessage())) {
            ResponseWrapper.Status status = new ResponseWrapper.Status(
                    exception.getErrorCode().getStatusCode().getCode(),
                    exception.getErrorCode().getErrorCode(),
                    exception.getExtraMessage(), ""
            );
            ResponseWrapper responseWrapper = new ResponseWrapper(status, null);
            return ResponseEntity.ok(responseWrapper);
        }
        return wrapException(exception.getErrorCode());
    }

    private ResponseEntity<ResponseWrapper> wrapException(ErrorCode errorCode) {
        ResponseWrapper.Status status = new ResponseWrapper.Status(errorCode.getStatusCode().getCode(),
                errorCode.getErrorCode(),
                errorCode.getErrorPhrase(), "");
        ResponseWrapper responseWrapper = new ResponseWrapper(status, null);
        return ResponseEntity.ok(responseWrapper);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseWrapper> methodArgumentNotValidException(HttpServletRequest request,
                                                                           MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ResponseWrapper.error(ErrorCode.DTO_VALIDATION_FAILED), HttpStatus.BAD_REQUEST);
    }

    /**
     * catch all others exception
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseWrapper> handleErrorException(HttpServletRequest request, Exception e) {
        log.error(request.getServletPath(), e);

        ResponseWrapper.Status status = new ResponseWrapper.Status(ErrorCode.INTERNAL_ERROR.getStatusCode().getCode(),
                ErrorCode.INTERNAL_ERROR.getErrorCode(),
                ErrorCode.INTERNAL_ERROR.getErrorPhrase(), "");
        final ResponseWrapper responseWrapper = new ResponseWrapper(status, null);

        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}