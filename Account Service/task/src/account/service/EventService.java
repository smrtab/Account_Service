package account.service;

import account.data.entity.Event;
import account.data.model.EventAction;
import account.data.repository.EventRepository;
import account.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.time.LocalDate;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(
        EventRepository eventRepository
    ) {
        this.eventRepository = eventRepository;
    }

    public Event create(
        EventAction action,
        String subject,
        String object
    ) {
        UriComponents uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().build();
        String path = uriComponents.getPath();

        return createEvent(
            action.name(),
            subject,
            object,
            path
        );
    }

    public Event create(
        EventAction action,
        String subject
    ) {
        UriComponents uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().build();
        String path = uriComponents.getPath();

        return createEvent(
            action.name(),
            subject,
            path,
            path
        );
    }

    private Event createEvent(
        String action,
        String subject,
        String object,
        String path
    ) {
        Event event = new Event();
        event.setDate(LocalDate.now());
        event.setAction(action);
        event.setSubject(subject);
        event.setObject(object);
        event.setPath(path);

        return eventRepository.save(event);
    }
}
