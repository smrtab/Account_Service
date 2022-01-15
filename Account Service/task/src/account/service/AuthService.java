package account.service;

import account.data.entity.Role;
import account.data.entity.User;
import account.data.exception.SamePasswordException;
import account.data.exception.UserExistException;
import account.data.model.EventAction;
import account.data.remote.request.ChangePasswordRequest;
import account.data.remote.request.SignupRequest;
import account.data.remote.response.ChangePasswordResponse;
import account.data.remote.response.SignupResponse;
import account.data.repository.RoleRepository;
import account.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidationService passwordValidationService;
    private final EventService eventService;

    @Autowired
    public AuthService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        PasswordValidationService passwordValidationService,
        EventService eventService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidationService = passwordValidationService;
        this.eventService = eventService;
    }

    public SignupResponse auth(SignupRequest request) {

        List<User> users = userRepository.findAll();

        Role role;
        if (users.isEmpty()) {
            role = roleRepository.findByName(Role.ADMINISTRATOR).get();
        } else {
            role = roleRepository.findByName(Role.USER).get();
        }

        long count = users.stream()
            .filter(user -> user.getEmail().equalsIgnoreCase(request.getEmail()))
            .count();

        if (count > 0) {
            throw new UserExistException();
        }

        passwordValidationService.validate(request.getPassword());

        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setFailedAttempt(0);
        user.setAccountNonLocked(true);
        user.getRoles().add(role);

        userRepository.save(user);

        eventService.create(
            EventAction.CREATE_USER,
            "Anonymous",
            user.getEmail()
        );

        return new SignupResponse(
            user.getId(),
            user.getName(),
            user.getLastname(),
            user.getEmail(),
            user.getPrefixedRoleNames()
        );
    }

    public ChangePasswordResponse changepass(ChangePasswordRequest request) {

        User user = AuthService.currentUser();

        passwordValidationService.validate(request.getNewPassword());

        if (passwordEncoder.matches(
            request.getNewPassword(),
            user.getPassword()
        )) {
            throw new SamePasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        eventService.create(
            EventAction.CHANGE_PASSWORD,
            user.getEmail(),
            user.getEmail()
        );

        return new ChangePasswordResponse(
            user.getEmail(),
            "The password has been updated successfully"
        );
    }

    public static User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
            || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User must login!"
            );
        }

        User user = (User) authentication.getPrincipal();

        if (user == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User must login!"
            );
        }

        return user;
    }
}
