package concertreservation.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String message;

    public static ErrorResponse of(ErrorType errorType) {
        return new ErrorResponse(errorType.getStatus(), errorType.getMessage());
    }
}
