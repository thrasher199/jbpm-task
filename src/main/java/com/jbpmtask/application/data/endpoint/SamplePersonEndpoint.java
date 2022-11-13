package com.jbpmtask.application.data.endpoint;

import com.jbpmtask.application.data.entity.SamplePerson;
import com.jbpmtask.application.data.service.SamplePersonService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;
import java.util.UUID;

@Endpoint
@AnonymousAllowed
public class SamplePersonEndpoint {

    private final SamplePersonService service;

    @Autowired
    public SamplePersonEndpoint(SamplePersonService service) {
        this.service = service;
    }

    @Nonnull
    public Page<@Nonnull SamplePerson> list(Pageable page) {
        return service.list(page);
    }

    public Optional<SamplePerson> get(@Nonnull UUID id) {
        return service.get(id);
    }

    @Nonnull
    public SamplePerson update(@Nonnull SamplePerson entity) {
        return service.update(entity);
    }

    public void delete(@Nonnull UUID id) {
        service.delete(id);
    }

    public int count() {
        return service.count();
    }

}
