package concertreservation.user.repository;

import concertreservation.user.service.entity.User;
import concertreservation.user.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByIdWithPessimisticLock(Long userId) {
        return userJpaRepository.findByIdWithPessimisticLock(userId);
    }

    @Override
    public Optional<User> findByIdWithOptimisticLock(Long userId) {
        return userJpaRepository.findByIdWithOptimisticLock(userId);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
