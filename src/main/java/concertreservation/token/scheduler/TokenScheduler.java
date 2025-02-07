package concertreservation.token.scheduler;

import concertreservation.token.service.WaitingTokenRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final WaitingTokenRedisService waitingTokenRedisService;

    private static final String ACTIVE_QUEUE = "active_queue";
    private static final String WAITING_QUEUE = "waiting_queue";

    @Scheduled(fixedRate = 60000)
    public void activateFromWaiting(){
        Set<String> activeWaitingConcertIds = waitingTokenRedisService.getActiveWaitingConcertIds(WAITING_QUEUE);

        for (String activeWaitingConcertId : activeWaitingConcertIds) {
            waitingTokenRedisService.updateActivateFromWaiting(Long.valueOf(activeWaitingConcertId));
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens(){
        Set<String> activeWaitingConcertIds = waitingTokenRedisService.getActiveWaitingConcertIds(ACTIVE_QUEUE);

        for (String activeWaitingConcertId : activeWaitingConcertIds) {
            waitingTokenRedisService.removeExpiredTokens(Long.valueOf(activeWaitingConcertId));
        }
    }
}
