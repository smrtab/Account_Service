package account.api;

import account.data.entity.Event;
import account.data.remote.enitity.RemoteUser;
import account.data.repository.EventRepository;
import account.service.EmployeeService;
import account.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SecurityController {

    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/api/security/events/")
    public List<Event> getEvents() {
        return securityService.getEvents();
    }
}
