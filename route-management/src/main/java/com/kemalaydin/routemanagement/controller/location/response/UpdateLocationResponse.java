package com.kemalaydin.routemanagement.controller.location.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateLocationResponse {
    private String id;
    private String name;
    private String country;
    private String city;
    private String code;
}
