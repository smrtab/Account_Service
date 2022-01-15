package account.data.repository;

import account.data.entity.Event;
import account.data.entity.Role;
import account.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findAll();
}