package org.dxc.orgservice.campus.features.queries.get_campus_by_id.application;

import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.port.in.IGetCampusByIdHandler;
import org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.port.out.IGetCampusByIdReadModel;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;

public class GetCampusByIdHandler implements IGetCampusByIdHandler {

    private final IGetCampusByIdReadModel readModel;

    public GetCampusByIdHandler(IGetCampusByIdReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public CampusResponse handle(GetCampusByIdQuery query) {
        return readModel.findById(query.campusId())
                .orElseThrow(() -> new ResourceNotFoundException("Campus", query.campusId()));
    }
}
