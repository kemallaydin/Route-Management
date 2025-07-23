package com.kemalaydin.routemanagement.converter;

import com.kemalaydin.routemanagement.controller.PagedResponse;
import com.kemalaydin.routemanagement.controller.transportation.request.CreateTransportationRequest;
import com.kemalaydin.routemanagement.controller.transportation.request.UpdateTransportationRequest;
import com.kemalaydin.routemanagement.controller.transportation.response.CreateTransportationResponse;
import com.kemalaydin.routemanagement.controller.transportation.response.GetTransportationResponse;
import com.kemalaydin.routemanagement.controller.transportation.response.UpdateTransportationResponse;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.repository.transportation.entity.TransportationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TransportationConverter {
    @Mapping(source = "originLocationCode", target = "originLocation", qualifiedByName = "fromCodeToLocation")
    @Mapping(source = "destinationLocationCode", target = "destinationLocation", qualifiedByName = "fromCodeToLocation")
    Transportation createTransportationRequestToModel(CreateTransportationRequest request);

    @Mapping(source = "originLocationCode", target = "originLocation", qualifiedByName = "fromCodeToLocation")
    @Mapping(source = "destinationLocationCode", target = "destinationLocation", qualifiedByName = "fromCodeToLocation")
    Transportation updateTransportationRequestToModel(UpdateTransportationRequest request);

    @Mapping(source = "originLocation", target = "originLocationCode", qualifiedByName = "fromLocationToCode")
    @Mapping(source = "destinationLocation", target = "destinationLocationCode", qualifiedByName = "fromLocationToCode")
    CreateTransportationResponse modelToCreateTransportationResponse(Transportation model);

    @Mapping(source = "originLocation", target = "originLocationCode", qualifiedByName = "fromLocationToCode")
    @Mapping(source = "destinationLocation", target = "destinationLocationCode", qualifiedByName = "fromLocationToCode")
    UpdateTransportationResponse modelToUpdateTransportationResponse(Transportation model);

    @Mapping(source = "originLocation", target = "originLocationCode", qualifiedByName = "fromLocationToCode")
    @Mapping(source = "destinationLocation", target = "destinationLocationCode", qualifiedByName = "fromLocationToCode")
    GetTransportationResponse modelToGetTransportationResponse(Transportation model);

    @Named("fromLocationToCode")
    static String fromLocationToCode(Location location) {
        return location.getCode();
    }

    @Named("fromCodeToLocation")
    static Location fromCodeToLocation(String code) {
        return Location.builder().code(code).build();
    }

    TransportationEntity modelToEntity(Transportation model);

    Transportation entityToModel(TransportationEntity entity);

    default PagedResponse<GetTransportationResponse> pageToPagedResponse(Page<Transportation> transportations) {
        List<GetTransportationResponse> content = transportations.stream()
                .map(this::modelToGetTransportationResponse)
                .collect(Collectors.toList());

        return PagedResponse.<GetTransportationResponse>builder()
                .content(content)
                .pageNumber(transportations.getNumber())
                .pageSize(transportations.getSize())
                .totalElements(transportations.getTotalElements())
                .totalPages(transportations.getTotalPages())
                .last(transportations.isLast())
                .build();
    }
}
