package account.service;

import account.data.entity.Role;
import account.data.entity.User;
import account.data.exception.*;
import account.data.model.EventAction;
import account.data.remote.enitity.RemoteUser;
import account.data.remote.request.AccessControlRequest;
import account.data.remote.request.ChangeUserRoleRequest;
import account.data.remote.response.StatusResponse;
import account.data.remote.response.UserDeleteResponse;
import account.data.repository.RoleRepository;
import account.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public AdminService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        UserService userService,
        EventService eventService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    public List<RemoteUser> getUsers() {

        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getId));

        List<RemoteUser> remoteUsers = new ArrayList<>();
        for (User user: users) {
            remoteUsers.add(
                new RemoteUser(
                    user.getId(),
                    user.getName(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getPrefixedRoleNames()
                )
            );
        }

        return remoteUsers;
    }

    public RemoteUser updateUserRole(ChangeUserRoleRequest request) {

        User admin = AuthService.currentUser();

        Optional<User> optionalUser = userRepository.findByEmail(request.getUser().toLowerCase());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<Role> optionalRole = roleRepository.findByName(request.getRole());
        if (optionalRole.isEmpty()) {
            throw new RoleNotFoundException();
        }

        Role role = optionalRole.get();
        User user = optionalUser.get();

        switch (request.getOperation()) {
            case ChangeUserRoleRequest.OPERATION_GRANT: {
                if (role.isBusiness() && user.hasAdminRole()) {
                    throw new BusinessAdministrativeCombinationException();
                }
                if (role.isAdmin() && user.hasBusinessRole()) {
                    throw new BusinessAdministrativeCombinationException();
                }
                eventService.create(
                    EventAction.GRANT_ROLE,
                    admin.getEmail(),
                    "Grant role " + request.getRole() + " to " + user.getEmail()
                );
                user.getRoles().add(role);
                break;
            }
            case ChangeUserRoleRequest.OPERATION_REMOVE: {
                if (!user.getRoleNames().contains(role.getName())) {
                    throw new IncorrectUserRoleException();
                }
                if (role.isAdmin()) {
                    throw new AdminUserRemoveException();
                }
                if (user.getRoleNames().size() == 1) {
                    throw new SingleRoleRemovingException();
                }
                eventService.create(
                    EventAction.REMOVE_ROLE,
                    admin.getEmail(),
                    "Remove role " + request.getRole() + " from " + user.getEmail()
                );
                user.getRoles().remove(role);
                break;
            }
        }

        userRepository.save(user);

        return new RemoteUser(
            user.getId(),
            user.getName(),
            user.getLastname(),
            user.getEmail(),
            user.getPrefixedRoleNames()
        );
    }

    public StatusResponse accessControl(AccessControlRequest request) {

        User admin = AuthService.currentUser();

        Optional<User> optionalUser = userRepository.findByEmail(request.getUser().toLowerCase());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = optionalUser.get();
        String status;
        switch (request.getOperation()) {
            case AccessControlRequest.OPERATION_LOCK: {
                if (user.hasAdminRole()) {
                    throw new AdminUserLockException();
                }
                userService.lock(user);
                eventService.create(
                    EventAction.LOCK_USER,
                    admin.getEmail(),
                    "Lock user " + user.getEmail()
                );
                status = "User " + user.getEmail() + " locked!";
                break;
            }
            case AccessControlRequest.OPERATION_UNLOCK: {
                userService.unlock(user);
                eventService.create(
                    EventAction.UNLOCK_USER,
                    admin.getEmail(),
                    "Unlock user " + user.getEmail()
                );
                status = "User " + user.getEmail() + " unlocked!";
                break;
            }
            default:
                throw new IllegalArgumentException(
                    "Unknown operation has been provided."
                );
        }

        return new StatusResponse(status);
    }

    public UserDeleteResponse deleteUser(String email) {

        User admin = AuthService.currentUser();

        Optional<User> optionalUser = userRepository.findByEmail(email.toLowerCase());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = optionalUser.get();
        if (admin.getId() == user.getId()) {
            throw new AdminUserRemoveException();
        }

        userRepository.deleteById(user.getId());

        eventService.create(
            EventAction.DELETE_USER,
            admin.getEmail(),
            email
        );

        return new UserDeleteResponse(
            email,
            "Deleted successfully!"
        );
    }
}
