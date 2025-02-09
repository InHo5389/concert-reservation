package concertreservation.token.scheduler;

import concertreservation.token.service.WaitingTokenRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final WaitingTokenRedisService waitingTokenRedisService;

    private static final String ACTIVE_QUEUE = "active_queue";
    private static final String WAITING_QUEUE = "waiting_queue";

    @Scheduled(fixedRate = 60000)
    @SchedulerLock(
            name = "activateFromWaiting",
            lockAtLeastFor = "PT50S",
            lockAtMostFor = "PT50S"
    )
    public void activateFromWaiting(){
        log.info("[Server {}] Scheduler started at {}",
                System.getenv("HOSTNAME"), LocalDateTime.now());

        Set<String> activeWaitingConcertIds = waitingTokenRedisService.getActiveWaitingConcertIds(WAITING_QUEUE);

        for (String activeWaitingConcertId : activeWaitingConcertIds) {
            waitingTokenRedisService.updateActivateFromWaiting(Long.valueOf(activeWaitingConcertId));
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @SchedulerLock(
            name = "removeExpiredTokens",
            lockAtLeastFor = "PT50S",
            lockAtMostFor = "PT50S"
    )
    public void removeExpiredTokens(){
        Set<String> activeWaitingConcertIds = waitingTokenRedisService.getActiveWaitingConcertIds(ACTIVE_QUEUE);

        for (String activeWaitingConcertId : activeWaitingConcertIds) {
            waitingTokenRedisService.removeExpiredTokens(Long.valueOf(activeWaitingConcertId));
        }
    }
}
