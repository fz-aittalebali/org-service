package org.dxc.orgservice.company.features.commands.create_company.application;

import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

public record CreateCompanyCommand(OrgName name) {}
