package com.kemalaydin.routemanagement.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "route")
@Getter
@Setter
public class RouteProperties {
    private int maxTransportations;
    private int maxFlights;
    private int maxBeforeFlightTransfers;
    private int maxAfterFlightTransfers;
}