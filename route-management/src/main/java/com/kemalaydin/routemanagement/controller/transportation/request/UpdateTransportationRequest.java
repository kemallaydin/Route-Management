package com.kemalaydin.routemanagement.controller.transportation.request;

import com.kemalaydin.routemanagement.controller.transportation.request.validation.ValidateTransportationOperatingDays;
import com.kemalaydin.routemanagement.controller.transportation.request.validation.ValidateTransportationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UpdateTransportationRequest {
    @NotBlank(message = "Origin location code must not be blank")
    private String originLocationCode;
    @NotBlank(message = "Destination location code must not be blank")
    private String destinationLocationCode;
    @ValidateTransportationType
    private String type;
    @ValidateTransportationOperatingDays
    private Set<Integer> operatingDays;
}
