package account.service;

import account.data.entity.Event;
import account.data.entity.User;
import account.data.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class SecurityService {

    private final EventRepository eventRepository;

    @Autowired
    public SecurityService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        List<Event> events = eventRepository.findAll();
        log.info(events.toString());
        return events;
    }
}
