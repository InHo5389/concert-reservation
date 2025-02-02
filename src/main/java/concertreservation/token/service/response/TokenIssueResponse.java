package concertreservation.token.service.response;

import concertreservation.token.entity.TokenStatus;
import lombok.Getter;

@Getter
public class TokenIssueResponse {

   private String token;
   private TokenStatus tokenStatus;


    public static TokenIssueResponse from(String jwtToken, TokenStatus status) {
        TokenIssueResponse response = new TokenIssueResponse();
        response.token = jwtToken;
        response.tokenStatus = status;
        return response;
    }
}
