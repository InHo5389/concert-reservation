package concertreservation.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    NOT_FOUND_USER(404, "회원을 찾을수 없습니다."),
    NOT_FOUND_CONCERT(404, "콘서트를 찾을수 없습니다."),
    NOT_FOUND_CONCERT_SCHEDULE(404, "콘서트를 찾을수 없습니다."),
    NOT_FOUND_CONCERT_SEAT(404, "좌석을 찾을수 없습니다."),
    NOT_FOUNT_CONCERT_SCHEDULE_SEAT(404, "해당 콘서트 스케줄에 좌석을 찾을수 없습니다."),
    ALREADY_RESERVED_SEAT(400, "이미 예약된 좌석입니다."),
    NOT_FOUND_RESERVATION(400, "예약된 좌석이 없습니다."),
    ALREADY_PAID_SEAT(400, "이미 결제된 좌석입니다."),
    EXPIRED_RESERVATION(400, "예약 만료 시간이 지났습니다. 다시 예약하여 주세요"),
    NOT_ENOUGH_POINT(400,"포인트가 부족합니다. 충전후 결제하여 주세요.")
    ;

    private final int status;
    private final String message;
}
