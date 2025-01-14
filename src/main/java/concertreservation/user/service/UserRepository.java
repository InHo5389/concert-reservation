package concertreservation.user.service;

import concertreservation.user.service.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);
    Optional<User> findByIdWithPessimisticLock(Long userId);
    Optional<User> findByIdWithOptimisticLock(Long userId);
    User save(User user);
}
