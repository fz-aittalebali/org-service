package org.dxc.orgservice.company.features.commands.update_company.application;

import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.UUID;

public record UpdateCompanyCommand(UUID companyId, OrgName name) {}
