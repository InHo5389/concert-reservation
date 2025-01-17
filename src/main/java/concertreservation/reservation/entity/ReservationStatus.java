package concertreservation.reservation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    RESERVED("예약됨"),
    PAID("결제됨"),
    EXPIRED("만료됨")
    ;

    private final String content;
}
