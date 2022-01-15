package account.config;

import account.data.entity.Role;
import account.data.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRoleIfNotFound(Role.ADMINISTRATOR);
        createRoleIfNotFound(Role.USER);
        createRoleIfNotFound(Role.ACCOUNTANT);
        createRoleIfNotFound(Role.AUDITOR);

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {

        Optional<Role> optionalRole = roleRepository.findByName(name);

        Role role;
        if (optionalRole.isPresent()) {
            role = optionalRole.get();
        } else {
            role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }

        return role;
    }
}
