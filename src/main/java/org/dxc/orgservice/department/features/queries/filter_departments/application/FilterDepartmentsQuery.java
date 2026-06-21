package org.dxc.orgservice.department.features.queries.filter_departments.application;

import java.util.Optional;
import java.util.UUID;

public record FilterDepartmentsQuery(Optional<UUID> campusId, int page, int size) {}
