package com.jbpmtask.application.data.generator;

import com.jbpmtask.application.data.entity.Role;
import com.jbpmtask.application.data.entity.SamplePerson;
import com.jbpmtask.application.data.entity.User;
import com.jbpmtask.application.data.service.RoleRepository;
import com.jbpmtask.application.data.service.SamplePersonRepository;
import com.jbpmtask.application.data.service.UserRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, SamplePersonRepository samplePersonRepository,
                                      UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (samplePersonRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Person entities...");
            ExampleDataGenerator<SamplePerson> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
                    SamplePerson.class, LocalDateTime.of(2022, 8, 15, 0, 0, 0));
            samplePersonRepositoryGenerator.setData(SamplePerson::setFirstName, DataType.FIRST_NAME);
            samplePersonRepositoryGenerator.setData(SamplePerson::setLastName, DataType.LAST_NAME);
            samplePersonRepositoryGenerator.setData(SamplePerson::setEmail, DataType.EMAIL);
            samplePersonRepositoryGenerator.setData(SamplePerson::setPhone, DataType.PHONE_NUMBER);
            samplePersonRepositoryGenerator.setData(SamplePerson::setDateOfBirth, DataType.DATE_OF_BIRTH);
            samplePersonRepositoryGenerator.setData(SamplePerson::setOccupation, DataType.OCCUPATION);
            samplePersonRepositoryGenerator.setData(SamplePerson::setImportant, DataType.BOOLEAN_10_90);
            samplePersonRepository.saveAll(samplePersonRepositoryGenerator.create(100, seed));

           /* logger.info("... generating 2 Role entities...");
            Role adminRole = new Role();
            adminRole.setName("ADMIN");

            Role userRole = new Role();
            userRole.setName("USER");


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
            userRepository.save(admin);*/

            logger.info("Generated demo data");
        };
    }
}
