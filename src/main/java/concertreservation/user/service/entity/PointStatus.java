package concertreservation.user.service.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointStatus {

    USE("사용"),
    CHARGE("충전"),
    CANCEL("취소")
    ;

    private final String content;
}
