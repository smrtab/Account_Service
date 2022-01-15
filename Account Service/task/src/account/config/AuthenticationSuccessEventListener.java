package account.config;

import account.data.entity.User;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessEventListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent event) {

        User user =  (User) event.getAuthentication().getPrincipal();
        if (user.getFailedAttempt() > 0) {
            userService.resetFailedAttempts(user);
        }
    }
}