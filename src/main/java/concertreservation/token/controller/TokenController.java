package concertreservation.token.controller;

import concertreservation.token.interceptor.WaitingTokenRequired;
import concertreservation.token.service.WaitingTokenRedisService;
import concertreservation.token.service.response.TokenIssueResponse;
import concertreservation.token.service.response.TokenStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final WaitingTokenRedisService waitingTokenRedisService;

    @PostMapping("/waiting-token")
    public TokenIssueResponse issueToken(@RequestParam Long userId, @RequestParam Long concertId) {
        return waitingTokenRedisService.issueToken(userId, concertId);
    }

    @WaitingTokenRequired
    @GetMapping("/waiting-token/status")
    public TokenStatusResponse getTokenStatus(@RequestHeader("WaitingToken") String token) {
        return waitingTokenRedisService.getTokenStatus(token);
    }
}
