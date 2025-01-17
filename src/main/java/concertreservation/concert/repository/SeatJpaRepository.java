package concertreservation.concert.repository;

import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.user.service.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus);
    Optional<Seat> findByIdAndConcertScheduleId(Long seatId, Long concertScheduleId);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select s from Seat s where s.id = :id")
    Optional<Seat> findByIdWithOptimisticLock(@Param("id") Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Seat s where s.id = :id")
    Optional<Seat> findByIdWithPessimisticLock(@Param("id") Long seatId);
}
