package concertreservation.token.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenStatus {

    ACTIVE("활성중"),
    WAITING("대기중"),
    EXPIRED("만료됨")
    ;

    private final String content;
}
