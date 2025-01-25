package concertreservation.concert.service;

import concertreservation.concert.entity.Concert;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConcertIntegrationTest {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertScheduleRepository concertScheduleRepository;

    @Autowired
    private CacheManager cacheManager;


    @Test
    @DisplayName("availableDate 호출 시 캐시 저장 검증")
    void availableDateShouldCache() {
        // given
        Concert concert = concertRepository.save(new Concert());

        // when
        concertService.availableDate(concert.getId());

        // then
        assertThat(cacheManager.getCache("concert::schedule")
                .get(concert.getId())
                .get())
                .isInstanceOf(ConcertAvailableDateResponse.class);
    }

    @Test
    @DisplayName("캐시 삭제 검증")
    void updateScheduleShouldEvictCache() {
        // given
        Concert concert = concertRepository.save(new Concert());
        concertService.availableDate(concert.getId());

        // when
        concertService.updateSchedule(concert.getId(), LocalDate.now());

        // then
        assertThat(cacheManager.getCache("concert::schedule")
                .get(concert.getId()))
                .isNull();
    }
}
