package com.kemalaydin.routemanagement.service;

import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.model.transportation.TransportationType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class RouteSearchContext {
    private final List<Transportation> path;
    private final Set<String> visitedTransportationIds;
    private int flightCount;
    private int beforeFlightCount;
    private int afterFlightCount;
    private boolean flightSeen;

    public RouteSearchContext() {
        path = new ArrayList<>();
        visitedTransportationIds = new HashSet<>();
        flightCount = 0;
        beforeFlightCount = 0;
        afterFlightCount = 0;
        flightSeen = false;
    }

    public void addTransportation(Transportation transportation) {
        path.add(transportation);
        visitedTransportationIds.add(transportation.getId());

        if (TransportationType.FLIGHT.equals(transportation.getType())) {
            flightSeen = true;
            flightCount++;
        } else {
            if (!flightSeen) beforeFlightCount++;
            else afterFlightCount++;
        }
    }

    public void removeLastTransportation() {
        Transportation lastTransportation = path.remove(path.size() - 1);
        visitedTransportationIds.remove(lastTransportation.getId());

        if (TransportationType.FLIGHT.equals(lastTransportation.getType())) {
            flightCount--;
            flightSeen = flightCount > 0;
        } else {
            if (!flightSeen) beforeFlightCount--;
            else afterFlightCount--;
        }
    }

    public boolean isVisited(Transportation transportation) {
        return visitedTransportationIds.contains(transportation.getId());
    }
}
