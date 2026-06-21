package org.dxc.orgservice.team.adapter.out.persistence;

import java.util.UUID;

public interface TeamHierarchyProjection {
    UUID getTeamId();
    String getTeamName();
    UUID getDepartmentId();
    String getDepartmentName();
    UUID getCampusId();
    String getCampusName();
    UUID getCompanyId();
    String getCompanyName();
    UUID getCityId();
    String getCityName();
    UUID getCountryId();
    String getCountryName();
}
