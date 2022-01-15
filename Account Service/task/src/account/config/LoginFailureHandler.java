package account.config;


import account.data.entity.User;
import account.data.model.EventAction;
import account.data.repository.UserRepository;
import account.service.EventService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException, ServletException {

        String email = request.getParameter("email");
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled() && user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                    eventService.create(
                        EventAction.LOGIN_FAILED,
                        email
                    );
                } else {
                    userService.lock(user);
                    eventService.create(
                        EventAction.BRUTE_FORCE,
                        email
                    );
                    eventService.create(
                        EventAction.LOCK_USER,
                            "Lock user " + email
                    );
                    exception = new LockedException("Your account has been locked due to "
                        + UserService.MAX_FAILED_ATTEMPTS
                        + " failed attempts."
                    );
                }
            }
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}