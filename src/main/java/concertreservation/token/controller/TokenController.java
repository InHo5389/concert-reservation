package concertreservation.token.controller;

import concertreservation.token.interceptor.WaitingTokenRequired;
import concertreservation.token.service.WaitingTokenService;
import concertreservation.token.service.response.TokenIssueResponse;
import concertreservation.token.service.response.TokenStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final WaitingTokenService waitingTokenService;

    @PostMapping("/waiting-token")
    public TokenIssueResponse issueToken(@RequestParam Long userId) {
        return waitingTokenService.issueToken(userId);
    }

    @WaitingTokenRequired
    @GetMapping("/waiting-token/status")
    public TokenStatusResponse getTokenStatus(@RequestHeader("WaitingToken") String token) {
        return waitingTokenService.getTokenStatus(token);
    }
}
