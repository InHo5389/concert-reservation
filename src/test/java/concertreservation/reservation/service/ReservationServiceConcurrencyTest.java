package concertreservation.reservation.service;

import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.ConcertRepository;
import concertreservation.concert.service.ConcertScheduleRepository;
import concertreservation.concert.service.SeatRepository;
import concertreservation.reservation.entity.Reservation;
import concertreservation.reservation.service.response.PaymentResponse;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

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

        Concert concert = concertRepository.save(new Concert("콘서트1", "에스파", "https://"));
        ConcertSchedule concertSchedule = concertScheduleRepository.save(new ConcertSchedule(concert.getId(), LocalDate.of(2025, 1, 15)));
        Seat seat = seatRepository.save(new Seat(concertSchedule.getId(), "A1", SeatStatus.AVAILABLE, 50000));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    reservationService.reservation(userId, concertSchedule.getId(), seat.getId());
                    successfulReservations.incrementAndGet();
                } finally {
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

        Concert concert = concertRepository.save(new Concert("콘서트1", "에스파", "https://"));
        ConcertSchedule concertSchedule = concertScheduleRepository.save(new ConcertSchedule(concert.getId(), LocalDate.of(2025, 1, 15)));
        Seat seat = seatRepository.save(new Seat(concertSchedule.getId(), "A1", SeatStatus.AVAILABLE, 50000));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    reservationService.reservationPessimistic(userId, concertSchedule.getId(), seat.getId());
                    successfulReservations.incrementAndGet();
                } finally {
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
        Concert concert = concertRepository.save(new Concert("콘서트1", "에스파", "https://"));
        ConcertSchedule concertSchedule = concertScheduleRepository.save(new ConcertSchedule(concert.getId(), LocalDate.of(2025, 1, 15)));
        Seat seat = seatRepository.save(new Seat(concertSchedule.getId(), "A1", SeatStatus.AVAILABLE, 50000));


        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger(0);
        AtomicInteger failedReservations = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    reservationService.reservation(userId, concertSchedule.getId(), seat.getId());
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

    @Test
    @DisplayName("유저1이 500원짜리 100번 동시 요청해도 1번 결제가 되어야 한다.")
    void payment() throws InterruptedException {
        //given
        User savedUser = userRepository.save(User.create("유저1", "번호1", 10000));
        Long seatId = 1L;
        int reservationPoint = 500;
        Reservation reservation = Reservation.create(savedUser.getId(), seatId, reservationPoint, "아이유 콘서트", "A1");
        Reservation savedReservation = reservationRepository.save(reservation);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.payment(savedReservation.getId());
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        //when
        //then
        userRepository.findById(savedUser.getId()).get();
        assertThat(userRepository.findById(savedUser.getId()).get().getPoint()).isEqualTo(9500);
    }
}