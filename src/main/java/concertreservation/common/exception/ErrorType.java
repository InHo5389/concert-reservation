package concertreservation.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    NOT_FOUND_USER(404,"회원을 찾을수 없습니다."),
    NOT_FOUND_CONCERT(404,"콘서트를 찾을수 없습니다."),
    NOT_FOUND_CONCERT_SCHEDULE(404,"콘서트를 찾을수 없습니다.")
    ;

    private final int status;
    private final String message;
}
