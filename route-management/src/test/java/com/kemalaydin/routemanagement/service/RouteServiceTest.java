package com.kemalaydin.routemanagement.service;

import com.kemalaydin.routemanagement.controller.route.response.Route;
import com.kemalaydin.routemanagement.controller.route.response.RouteResponse;
import com.kemalaydin.routemanagement.controller.route.response.RoutesResponse;
import com.kemalaydin.routemanagement.converter.RouteConverter;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.model.transportation.TransportationType;
import com.kemalaydin.routemanagement.property.RouteProperties;
import com.kemalaydin.routemanagement.repository.location.LocationRepository;
import com.kemalaydin.routemanagement.repository.transportation.TransportationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {
    @InjectMocks
    RouteService underTest;
    @Mock
    TransportationRepository transportationRepository;
    @Mock
    LocationRepository locationRepository;
    @Mock
    RouteProperties routeProperties;
    @Mock
    RouteConverter routeConverter;

    @Test
    void getRoutes_returnsValidRoutes() {
        Location origin = Location.builder().code("TKSM").build();
        Location airport1 = Location.builder().code("IST").build();
        Location airport2 = Location.builder().code("LHR").build();
        Location destination = Location.builder().code("WBSD").build();

        Transportation beforeFlight = Transportation.builder()
                .id("1")
                .originLocation(origin)
                .destinationLocation(airport1)
                .type(TransportationType.UBER)
                .operatingDays(Set.of(2))
                .build();

        Transportation flight = Transportation.builder()
                .id("2")
                .originLocation(airport1)
                .destinationLocation(airport2)
                .type(TransportationType.FLIGHT)
                .operatingDays(Set.of(2))
                .build();

        Transportation afterFlight = Transportation.builder()
                .id("3")
                .originLocation(airport2)
                .destinationLocation(destination)
                .type(TransportationType.BUS)
                .operatingDays(Set.of(2))
                .build();

        when(locationRepository.findFirstByCode("TKSM")).thenReturn(Optional.of(origin));
        when(locationRepository.findFirstByCode("WBSD")).thenReturn(Optional.of(destination));

        when(transportationRepository.findAllByOriginLocation(origin)).thenReturn(List.of(beforeFlight));
        when(transportationRepository.findAllByOriginLocation(airport1)).thenReturn(List.of(flight));
        when(transportationRepository.findAllByOriginLocation(airport2)).thenReturn(List.of(afterFlight));

        when(routeProperties.getMaxTransportations()).thenReturn(3);
        when(routeProperties.getMaxFlights()).thenReturn(1);
        when(routeProperties.getMaxBeforeFlightTransfers()).thenReturn(1);
        when(routeProperties.getMaxAfterFlightTransfers()).thenReturn(1);

        when(routeConverter.toRoutesResponse(any())).thenAnswer(invocation -> {
            List<List<Transportation>> allRoutes = invocation.getArgument(0);
            List<RouteResponse> routeResponses = new ArrayList<>();

            for (List<Transportation> transportations : allRoutes) {
                List<Route> routeList = new ArrayList<>();
                int order = 1;
                for (Transportation t : transportations) {
                    routeList.add(Route.builder()
                            .order(order++)
                            .type(t.getType().name())
                            .from(t.getOriginLocation().getCode())
                            .to(t.getDestinationLocation().getCode())
                            .build());
                }
                routeResponses.add(RouteResponse.builder().route(routeList).build());
            }

            return RoutesResponse.builder().routes(routeResponses).build();
        });

        LocalDate date = LocalDate.of(2025, 7, 22);

        RoutesResponse result = underTest.getRoutes("TKSM", "WBSD", date);

        assertNotNull(result);
        assertFalse(result.getRoutes().isEmpty());

        RouteResponse firstRoute = result.getRoutes().get(0);
        assertEquals(3, firstRoute.getRoute().size());

        Route firstSegment = firstRoute.getRoute().get(0);
        assertEquals("UBER", firstSegment.getType());
        assertEquals("TKSM", firstSegment.getFrom());
        assertEquals("IST", firstSegment.getTo());

        Route secondSegment = firstRoute.getRoute().get(1);
        assertEquals("FLIGHT", secondSegment.getType());
        assertEquals("IST", secondSegment.getFrom());
        assertEquals("LHR", secondSegment.getTo());

        Route thirdSegment = firstRoute.getRoute().get(2);
        assertEquals("BUS", thirdSegment.getType());
        assertEquals("LHR", thirdSegment.getFrom());
        assertEquals("WBSD", thirdSegment.getTo());

        verify(locationRepository, times(1)).findFirstByCode("TKSM");
        verify(locationRepository, times(1)).findFirstByCode("WBSD");
        verify(transportationRepository, times(1)).findAllByOriginLocation(origin);
        verify(transportationRepository, times(1)).findAllByOriginLocation(airport1);
        verify(transportationRepository, times(1)).findAllByOriginLocation(airport2);
    }
}

