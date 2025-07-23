package com.kemalaydin.routemanagement.service;

import com.kemalaydin.routemanagement.controller.route.response.RoutesResponse;
import com.kemalaydin.routemanagement.converter.RouteConverter;
import com.kemalaydin.routemanagement.exception.BadRequestException;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.model.transportation.TransportationType;
import com.kemalaydin.routemanagement.property.RouteProperties;
import com.kemalaydin.routemanagement.repository.location.LocationRepository;
import com.kemalaydin.routemanagement.repository.transportation.TransportationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;
    private final RouteProperties routeProperties;
    private final RouteConverter routeConverter;

    @Cacheable(
            cacheNames = "routes",
            key = "#originLocationCode + '_' + #destinationLocationCode + '_' + #date.getDayOfWeek().value"
    )
    public RoutesResponse getRoutes(String originLocationCode, String destinationLocationCode, LocalDate date) {
        Location originLocation = locationRepository.findFirstByCode(originLocationCode)
                .orElseThrow(() -> new BadRequestException(String.format("Origin location not found: %s", originLocationCode)));

        Location destionationLocation = locationRepository.findFirstByCode(destinationLocationCode)
                .orElseThrow(() -> new BadRequestException(String.format("Destination location not found: %s", destinationLocationCode)));

        List<List<Transportation>> allRoutes = new ArrayList<>();
        findAvailableRoutes(originLocation, destionationLocation, new RouteSearchContext(), allRoutes, date.getDayOfWeek().getValue());

        return routeConverter.toRoutesResponse(allRoutes);
    }

    private void findAvailableRoutes(Location current, Location destination, RouteSearchContext context,
                                     List<List<Transportation>> results, int selectedDayOfWeek) {

        if (!context.getPath().isEmpty() && context.isFlightSeen() && current.getCode().equals(destination.getCode())) {
            results.add(new ArrayList<>(context.getPath()));
            return;
        }

        List<Transportation> nextTransportations = transportationRepository.findAllByOriginLocation(current);

        for (Transportation transportation : nextTransportations) {
            if (!isValidTransportation(transportation, selectedDayOfWeek, context)) continue;

            context.addTransportation(transportation);
            findAvailableRoutes(transportation.getDestinationLocation(), destination, context, results, selectedDayOfWeek);
            context.removeLastTransportation();
        }
    }

    private boolean isValidTransportation(Transportation transportation, int selectedDayOfWeek, RouteSearchContext context) {
        if (context.isVisited(transportation)) return false;
        if (!transportation.getOperatingDays().contains(selectedDayOfWeek)) return false;

        if (context.getPath().size() >= routeProperties.getMaxTransportations()) return false;

        if (TransportationType.FLIGHT.equals(transportation.getType())) {
            return context.getFlightCount() < routeProperties.getMaxFlights();
        }

        return !context.isFlightSeen()
                ? context.getBeforeFlightCount() < routeProperties.getMaxBeforeFlightTransfers()
                : context.getAfterFlightCount() < routeProperties.getMaxAfterFlightTransfers();
    }
}
