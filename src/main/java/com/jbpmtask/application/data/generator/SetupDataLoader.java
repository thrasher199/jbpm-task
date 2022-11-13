package com.jbpmtask.application.data.generator;

import com.jbpmtask.application.data.entity.Role;
import com.jbpmtask.application.data.entity.User;
import com.jbpmtask.application.data.service.RoleRepository;
import com.jbpmtask.application.data.service.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SetupDataLoader(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.count() != 0L) {
            logger.info("Using existing database");
            return;
        }


        logger.info("... generating 2 Role entities...");
        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        Role userRole = new Role();
        userRole.setName("USER");

        roleRepository.saveAll(Arrays.asList(adminRole, userRole));


        logger.info("... generating 2 User entities...");
        User user = new User();
        user.setName("John Normal");
        user.setUsername("user");
        user.setHashedPassword(passwordEncoder.encode("user"));
        user.addRole(userRole);
        userRepository.save(user);

        User admin = new User();
        admin.setName("Emma Powerful");
        admin.setUsername("admin");
        admin.setHashedPassword(passwordEncoder.encode("admin"));
        admin.addRole(userRole);
        admin.addRole(adminRole);
        userRepository.save(admin);

        logger.info("Generated demo data");
    }
}
