package concertreservation.user.service;

import concertreservation.user.facade.UserFacade;
import concertreservation.user.service.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class UserPointConcurrencyTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저ID 1값이 동시에 1000번 포인트를 충전할 때 동시성 제어 없이 포인트를 충전하면 데이터 정합성이 깨진다")
    void chargePointConcurrencyFailTest() throws InterruptedException {
        //given
        int threadCount = 1000;

        User user = userRepository.save(new User("이름1", "번호", 0));

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userService.chargePoint(user.getId(), 100);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        User savedUser = userRepository.findById(user.getId()).orElseThrow();
        //then
        assertThat(savedUser.getPoint()).isLessThan(100000);
    }

    @Test
    @DisplayName("유저ID 1값이 0포인트를 가지고 있을때 동시에 1000번 포인트를 충전하면 100000포인트가 된다.")
    void chargePointConcurrencySuccessTestByPessimisticLock() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        //given
        int threadCount = 1000;

        User user = userRepository.save(new User("이름1", "번호", 0));

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userService.chargePointPessimisticLock(user.getId(), 100);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        long endTime = System.currentTimeMillis();
        System.out.println("테스트 실행 시간: " + (endTime - startTime) + "ms");
        //then
        assertThat(savedUser.getPoint()).isEqualTo(100000);
    }

    @Test
    @DisplayName("유저ID 1값이 0포인트를 가지고 있을때 동시에 1000번 포인트를 충전하면 100000포인트가 된다.")
    void chargePointConcurrencySuccessTestByOptimisticLock() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        //given
        int threadCount = 1000;

        User user = userRepository.save(new User("이름1", "번호", 0));

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userFacade.chargePointOptimisticLock(user.getId(), 100);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        long endTime = System.currentTimeMillis();
        System.out.println("테스트 실행 시간: " + (endTime - startTime) + "ms");
        //then
        assertThat(savedUser.getPoint()).isEqualTo(100000);
    }
}
