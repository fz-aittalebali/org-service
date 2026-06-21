package org.dxc.orgservice.campus.features.commands.create_campus.application;

import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;


public record CreateCampusCommand(OrgName name, CompanyId companyId, CityId cityId) {}
