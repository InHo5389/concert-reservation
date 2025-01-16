package concertreservation.concert.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatStatus {

    AVAILABLE("사용가능"),
    RESERVED("예약됨")
    ;

    private final String content;
}
