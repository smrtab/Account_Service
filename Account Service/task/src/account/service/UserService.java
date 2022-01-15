package account.service;

import account.data.entity.User;
import account.data.model.EventAction;
import account.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    public static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final EventService eventService;

    @Autowired
    public UserService(
        UserRepository userRepository,
        EventService eventService
    ) {
        this.userRepository = userRepository;
        this.eventService = eventService;
    }

    @Transactional
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        user.setFailedAttempt(newFailAttempts);
        userRepository.save(user);
    }

    @Transactional
    public void resetFailedAttempts(User user) {
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    @Transactional
    public void lock(User user) {
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    @Transactional
    public void unlock(User user) {
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
            .findByEmail(username.toLowerCase())
            .orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username))
            );
    }
}
