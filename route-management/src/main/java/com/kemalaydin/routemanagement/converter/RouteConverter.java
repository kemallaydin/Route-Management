package com.kemalaydin.routemanagement.converter;

import com.kemalaydin.routemanagement.controller.route.response.Route;
import com.kemalaydin.routemanagement.controller.route.response.RouteResponse;
import com.kemalaydin.routemanagement.controller.route.response.RoutesResponse;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public interface RouteConverter {

    default RoutesResponse toRoutesResponse(List<List<Transportation>> allRoutes) {
        List<RouteResponse> routeResponses = allRoutes.stream()
                .map(this::toRouteResponse)
                .toList();

        return RoutesResponse.builder()
                .routes(routeResponses)
                .build();
    }

    default RouteResponse toRouteResponse(List<Transportation> transportations) {
        List<Route> route = IntStream.range(0, transportations.size())
                .mapToObj(i -> toRoute(transportations.get(i), i + 1))
                .toList();

        return RouteResponse.builder()
                .route(route)
                .build();
    }

    default Route toRoute(Transportation t, int order) {
        return Route.builder()
                .order(order)
                .type(t.getType().name())
                .from(t.getOriginLocation().getName())
                .to(t.getDestinationLocation().getName())
                .build();
    }
}
