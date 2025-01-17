package concertreservation.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    NOT_FOUND_USER(404,"회원을 찾을수 없습니다."),
    NOT_FOUND_CONCERT(404,"콘서트를 찾을수 없습니다."),
    NOT_FOUND_CONCERT_SCHEDULE(404,"콘서트를 찾을수 없습니다."),
    NOT_FOUNT_CONCERT_SCHEDULE_SEAT(404,"해당 콘서트 스케줄에 좌석을 찾을수 없습니다."),
    NOT_AVAILABLE_SEAT(400, "예약 가능한 좌석이 아닙니다.")
    ;

    private final int status;
    private final String message;
}
