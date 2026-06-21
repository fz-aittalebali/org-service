package org.dxc.orgservice.country.features.queries.get_country_by_id.application;

import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.in.IGetCountryByIdHandler;
import org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.out.IGetCountryByIdReadModel;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;

public class GetCountryByIdHandler implements IGetCountryByIdHandler {

    private final IGetCountryByIdReadModel readModel;

    public GetCountryByIdHandler(IGetCountryByIdReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public CountryResponse handle(GetCountryByIdQuery query) {
        return readModel.findById(query.countryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country", query.countryId()));
    }
}
