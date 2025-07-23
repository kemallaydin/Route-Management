package com.kemalaydin.routemanagement.controller.route.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutesResponse implements Serializable {
    private List<RouteResponse> routes;
}
