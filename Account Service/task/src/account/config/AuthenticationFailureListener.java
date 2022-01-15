package account.config;

import account.data.entity.User;
import account.data.model.EventAction;
import account.data.repository.UserRepository;
import account.service.EventService;
import account.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        String email = event.getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        eventService.create(
            EventAction.LOGIN_FAILED,
            email
        );

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            if (!user.hasAdminRole() && user.isEnabled() && user.isAccountNonLocked()) {

                if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    eventService.create(
                        EventAction.BRUTE_FORCE,
                        email
                    );
                    eventService.create(
                        EventAction.LOCK_USER,
                        email,
                        "Lock user " + email
                    );
                }
            }
        }
    }
}