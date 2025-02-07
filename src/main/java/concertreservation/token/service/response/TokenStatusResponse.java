package concertreservation.token.service.response;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.entity.WaitingToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenStatusResponse {

    private String token;
    private TokenStatus tokenStatus;
    private Long waitingNumber;
    private LocalDateTime expiredAt;

    public static TokenStatusResponse from(WaitingToken waitingToken, Long waitingNumber) {
        TokenStatusResponse response = new TokenStatusResponse();
        response.token = waitingToken.getToken();
        response.tokenStatus = waitingToken.getStatus();
        response.waitingNumber = waitingNumber;
        response.expiredAt = waitingToken.getExpiredAt();
        return response;
    }
}
