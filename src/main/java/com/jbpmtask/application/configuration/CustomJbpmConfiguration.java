package com.jbpmtask.application.configuration;

import com.jbpmtask.application.configuration.jbpm.CustomSpringRuntimeManagerFactoryImpl;
import com.jbpmtask.application.configuration.jbpm.CustomUserGroupCallback;
import com.jbpmtask.application.configuration.jbpm.CustomUserInfo;
import com.jbpmtask.application.data.service.RoleRepository;
import com.jbpmtask.application.data.service.UserRepository;
import org.jbpm.springboot.autoconfigure.EntityManagerFactoryHelper;
import org.jbpm.springboot.autoconfigure.JBPMProperties;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.task.api.UserInfo;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class CustomJbpmConfiguration {

    private static final String PERSISTENCE_UNIT_NAME = "org.jbpm.domain";
    private static final String PERSISTENCE_XML_LOCATION = "classpath:/META-INF/custom-jbpm-persistence.xml";
    private static final String QUARTZ_PROPS = "org.quartz.properties";
    private static final String QUARTZ_FAILED_DELAY = "org.jbpm.timer.quartz.delay";
    private static final String QUARTZ_FAILED_RETRIES = "org.jbpm.timer.quartz.retries";
    private static final String QUARTZ_RESCHEDULE_DELAY = "org.jbpm.timer.quartz.reschedule.delay";
    private static final String QUARTZ_START_DELAY = "org.jbpm.timer.delay";

    private final ApplicationContext applicationContext;

    public CustomJbpmConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaProperties jpaProperties){
        return EntityManagerFactoryHelper.create(applicationContext,
                dataSource,
                jpaProperties,
                PERSISTENCE_UNIT_NAME,
                PERSISTENCE_XML_LOCATION);
    }

    @Bean
    public UserGroupCallback userGroupCallback(UserRepository userRepository, RoleRepository roleRepository){
        return new CustomUserGroupCallback(userRepository, roleRepository);
    }

    @Bean
    public UserInfo userInfo(UserRepository userRepository){
        return new CustomUserInfo(userRepository);
    }

    @Bean
    public RuntimeManagerFactory runtimeManagerFactory(UserGroupCallback userGroupCallback, UserInfo userInfo, PlatformTransactionManager transactionManager, JBPMProperties properties) {

        CustomSpringRuntimeManagerFactoryImpl runtimeManager = new CustomSpringRuntimeManagerFactoryImpl();
        runtimeManager.setTransactionManager((AbstractPlatformTransactionManager) transactionManager);
        runtimeManager.setUserGroupCallback(userGroupCallback);
        runtimeManager.setUserInfo(userInfo);

        if (properties.getQuartz().isEnabled()) {
            System.setProperty(QUARTZ_PROPS, properties.getQuartz().getConfiguration());
            System.setProperty(QUARTZ_FAILED_DELAY, String.valueOf(properties.getQuartz().getFailedJobDelay()));
            System.setProperty(QUARTZ_FAILED_RETRIES, String.valueOf(properties.getQuartz().getFailedJobRetry()));
            System.setProperty(QUARTZ_RESCHEDULE_DELAY, String.valueOf(properties.getQuartz().getRescheduleDelay()));
            System.setProperty(QUARTZ_START_DELAY, String.valueOf(properties.getQuartz().getStartDelay()));
        } else {
            System.clearProperty(QUARTZ_PROPS);
            System.clearProperty(QUARTZ_FAILED_DELAY);
            System.clearProperty(QUARTZ_FAILED_RETRIES);
            System.clearProperty(QUARTZ_RESCHEDULE_DELAY);
            System.clearProperty(QUARTZ_START_DELAY);
        }

        return runtimeManager;
    }
}
