package org.dxc.orgservice.company.features.commands.delete_company.application;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
import org.dxc.orgservice.company.features.commands.delete_company.application.port.in.IDeleteCompanyHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class DeleteCompanyHandler implements IDeleteCompanyHandler {

    private final ICompanyRepository companyRepository;
    private final IEventPublisher eventPublisher;

    public DeleteCompanyHandler(ICompanyRepository companyRepository, IEventPublisher eventPublisher) {
        this.companyRepository = companyRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(DeleteCompanyCommand command) {
        Company company = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", command.companyId()));
        company.delete();
        companyRepository.deleteById(command.companyId());
        eventPublisher.publishAll(company.pullDomainEvents());
    }
}
