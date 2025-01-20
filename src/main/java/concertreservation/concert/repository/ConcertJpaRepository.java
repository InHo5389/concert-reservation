package concertreservation.concert.repository;

import concertreservation.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<Concert,Long> {

    @Query(
            value = "select * " +
                    "from concerts " +
                    "order by id desc limit :limit",
            nativeQuery = true
    )
    List<Concert> findAllInfiniteScroll(@Param("limit") Long limit);

    @Query(
            value = "select * " +
                    "from concerts " +
                    "where id < :lastConcertId " +
                    "order by id desc limit :limit",
            nativeQuery = true
    )
    List<Concert> findAllInfiniteScroll(@Param("limit") Long limit,@Param("lastConcertId")Long lastConcertId);
}
