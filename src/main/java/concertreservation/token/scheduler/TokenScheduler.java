package concertreservation.token.scheduler;

import concertreservation.token.service.WaitingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final WaitingTokenService waitingTokenService;

    @Scheduled(fixedRate = 60000)
    public void updateExpiredTokenStatus(){
        waitingTokenService.updateExpiredTokenStatus();
    }

    @Scheduled(fixedRate = 60000)
    public void activateFromWaiting(){
        waitingTokenService.updateActivateFromWaiting();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens(){
        waitingTokenService.removeExpiredTokens();
    }
}
