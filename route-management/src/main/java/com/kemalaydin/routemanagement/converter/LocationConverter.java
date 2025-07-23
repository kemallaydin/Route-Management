package com.kemalaydin.routemanagement.converter;

import com.kemalaydin.routemanagement.controller.PagedResponse;
import com.kemalaydin.routemanagement.controller.location.request.CreateLocationRequest;
import com.kemalaydin.routemanagement.controller.location.request.UpdateLocationRequest;
import com.kemalaydin.routemanagement.controller.location.response.CreateLocationResponse;
import com.kemalaydin.routemanagement.controller.location.response.GetLocationResponse;
import com.kemalaydin.routemanagement.controller.location.response.UpdateLocationResponse;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.repository.location.entity.LocationEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LocationConverter {
    Location createLocationRequestToModel(CreateLocationRequest request);

    CreateLocationResponse modelToCreateLocationResponse(Location location);

    Location upsertLocationRequestToModel(UpdateLocationRequest request);

    UpdateLocationResponse modelToUpsertLocationResponse(Location location);

    GetLocationResponse modelToGetLocationResponse(Location location);

    LocationEntity modelToEntity(Location location);

    Location entityToModel(LocationEntity locationEntity);

    default PagedResponse<GetLocationResponse> pageToPagedResponse(Page<Location> locations) {
        List<GetLocationResponse> content = locations.stream()
                .map(this::modelToGetLocationResponse)
                .collect(Collectors.toList());

        return PagedResponse.<GetLocationResponse>builder()
                .content(content)
                .pageNumber(locations.getNumber())
                .pageSize(locations.getSize())
                .totalElements(locations.getTotalElements())
                .totalPages(locations.getTotalPages())
                .last(locations.isLast())
                .build();
    }
}
