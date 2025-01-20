package concertreservation.reservation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    COMPLETED("완료"),
    CANCEL("취소")
    ;

    private final String content;
}
