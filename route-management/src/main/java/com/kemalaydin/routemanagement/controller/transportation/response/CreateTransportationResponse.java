package com.kemalaydin.routemanagement.controller.transportation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class CreateTransportationResponse {
    private String id;
    private String originLocationCode;
    private String destinationLocationCode;
    private String type;
    private Set<Integer> operatingDays;
}
