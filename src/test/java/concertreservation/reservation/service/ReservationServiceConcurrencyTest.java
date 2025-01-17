package concertreservation.reservation.service;

import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.ConcertRepository;
import concertreservation.concert.service.ConcertScheduleRepository;
import concertreservation.concert.service.SeatRepository;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ConcertScheduleRepository concertScheduleRepository;

    @Autowired
    private SeatRepository seatRepository;

    private static final int numberOfThreads = 100;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < numberOfThreads; i++) {
            userRepository.save(new User("이름" + i, "번호" + i, 50000));
        }
    }

    @Test
    @DisplayName("유저 100명이 하나의 콘서트를 예약할때 예약은 1개만 이루어져야 한다.")
    void reservationConcurrencyWithOptimistic() throws InterruptedException {
        //given
        long startTime = System.currentTimeMillis();

        long concertId = 1L;
        long concertScheduleId = 1L;
        long seatId = 1L;
        concertRepository.save(new Concert("콘서트1", "에스파", "https://"));
        concertScheduleRepository.save(new ConcertSchedule(concertId, LocalDate.of(2025, 1, 15)));
        seatRepository.save(new Seat(concertScheduleId, "A1", SeatStatus.AVAILABLE, 50000));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    reservationService.reservation(userId, concertScheduleId, seatId);
                    successfulReservations.incrementAndGet();
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        //when
        //then
        long endTime = System.currentTimeMillis();
        System.out.println("테스트 실행 시간: " + (endTime - startTime) + "ms");
        assertThat(successfulReservations.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 100명이 하나의 콘서트를 예약할때 예약은 1개만 이루어져야 한다.")
    void reservationConcurrencyWithPessimistic() throws InterruptedException {
        //given
        long startTime = System.currentTimeMillis();

        long concertId = 1L;
        long concertScheduleId = 1L;
        long seatId = 1L;
        concertRepository.save(new Concert("콘서트1", "에스파", "https://"));
        concertScheduleRepository.save(new ConcertSchedule(concertId, LocalDate.of(2025, 1, 15)));
        seatRepository.save(new Seat(concertScheduleId, "A1", SeatStatus.AVAILABLE, 50000));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    reservationService.reservationPessimistic(userId, concertScheduleId, seatId);
                    successfulReservations.incrementAndGet();
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        //when
        //then
        long endTime = System.currentTimeMillis();
        System.out.println("테스트 실행 시간: " + (endTime - startTime) + "ms");
        assertThat(successfulReservations.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 100명이 하나의 콘서트를 예약할때 예외가 발생해야 한다")
    void reservationConcurrencyWithOptimistic1() throws InterruptedException {
        //given
        long concertId = 1L;
        long concertScheduleId = 1L;
        long seatId = 1L;
        concertRepository.save(new Concert("콘서트1", "에스파", "https://"));
        concertScheduleRepository.save(new ConcertSchedule(concertId, LocalDate.of(2025, 1, 15)));
        seatRepository.save(new Seat(concertScheduleId, "A1", SeatStatus.AVAILABLE, 50000));


        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger(0);
        AtomicInteger failedReservations = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    reservationService.reservation(userId, concertScheduleId, seatId);
                    successfulReservations.incrementAndGet();
                } catch (Exception e) {
                    failedReservations.incrementAndGet();
                    System.out.println("Optimistic Lock Exception occurred: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        assertThat(successfulReservations.get()).isEqualTo(1);
        assertThat(failedReservations.get()).isEqualTo(numberOfThreads - 1);
    }
}