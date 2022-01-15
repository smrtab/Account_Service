package account.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
@Table(name = "payment")
public class Payment implements Comparable<Payment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "period")
    private LocalDate period;

    @Column(name = "salary")
    private long salary;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public int compareTo(Payment o) {
        return period.compareTo(o.getPeriod());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return salary == payment.salary && id.equals(payment.id) && period.equals(payment.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, salary);
    }
}
