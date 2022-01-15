package account.data.repository;

import account.data.entity.Payment;
import account.data.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    Optional<Payment> findByUserId(long userId);
    Optional<Payment> findByUserIdAndPeriod(long userId, LocalDate period);
    Optional<Payment> findByPeriod(LocalDate period);
}
