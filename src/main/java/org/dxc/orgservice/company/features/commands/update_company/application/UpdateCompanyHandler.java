package org.dxc.orgservice.company.features.commands.update_company.application;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
import org.dxc.orgservice.company.features.commands.update_company.application.port.in.IUpdateCompanyHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class UpdateCompanyHandler implements IUpdateCompanyHandler {

    private final ICompanyRepository companyRepository;
    private final IEventPublisher eventPublisher;

    public UpdateCompanyHandler(ICompanyRepository companyRepository, IEventPublisher eventPublisher) {
        this.companyRepository = companyRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(UpdateCompanyCommand command) {
        Company company = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", command.companyId()));
        if (!company.getName().equals(command.name()) && companyRepository.existsByName(command.name())) {
            throw new DuplicateResourceException("Company", command.name().value());
        }
        company.updateName(command.name());
        companyRepository.save(company);
        eventPublisher.publishAll(company.pullDomainEvents());
    }
}
