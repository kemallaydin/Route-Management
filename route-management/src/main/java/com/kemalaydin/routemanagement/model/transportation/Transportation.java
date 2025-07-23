package com.kemalaydin.routemanagement.model.transportation;

import com.kemalaydin.routemanagement.model.location.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class Transportation {
    private String id;
    private Location originLocation;
    private Location destinationLocation;
    private TransportationType type;
    private Set<Integer> operatingDays;
}
