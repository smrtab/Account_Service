package account.data.repository;

import account.data.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();
    Optional<User> findByEmail(String email);

    @Query(value = "UPDATE `user` SET failed_attempt=?1 WHERE email=?2", nativeQuery = true)
    @Modifying
    void updateFailedAttempts(
        int failAttempts,
        String email
    );
}
