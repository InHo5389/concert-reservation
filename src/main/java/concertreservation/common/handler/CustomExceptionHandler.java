package concertreservation.common.handler;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<?> apiExceptionHandler(CustomGlobalException e) {
        ErrorType errorType = e.getErrorType();
        HttpStatus httpStatus = HttpStatus.valueOf(errorType.getStatus());

        return ResponseEntity
                .status(httpStatus.value())
                .body(new ExceptionResponse(httpStatus.value(), httpStatus, e.getMessage()));
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> apiExceptionHandler(ObjectOptimisticLockingFailureException e) {
        ErrorType errorType = ErrorType.ALREADY_RESERVED_SEAT;
        HttpStatus httpStatus = HttpStatus.valueOf(errorType.getStatus());

        return ResponseEntity
                .status(httpStatus.value())
                .body(new ExceptionResponse(httpStatus.value(), httpStatus, errorType.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    private static class ExceptionResponse {
        private int code;
        private HttpStatus httpStatus;
        private String message;
    }
}
