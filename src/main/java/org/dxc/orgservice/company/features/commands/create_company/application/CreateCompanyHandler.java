package org.dxc.orgservice.company.features.commands.create_company.application;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
import org.dxc.orgservice.company.features.commands.create_company.application.port.in.ICreateCompanyHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

import java.util.UUID;

public class CreateCompanyHandler implements ICreateCompanyHandler {

    private final ICompanyRepository companyRepository;
    private final IEventPublisher eventPublisher;

    public CreateCompanyHandler(ICompanyRepository companyRepository, IEventPublisher eventPublisher) {
        this.companyRepository = companyRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID handle(CreateCompanyCommand command) {
        if (companyRepository.existsByName(command.name())) {
            throw new DuplicateResourceException("Company", command.name().value());
        }
        Company company = Company.createCompanyBuilder()
                .name(command.name())
                .build();
        companyRepository.save(company);
        eventPublisher.publishAll(company.pullDomainEvents());
        return company.getId();
    }
}
