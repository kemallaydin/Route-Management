package com.kemalaydin.routemanagement.repository.transportation;

import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.model.transportation.TransportationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransportationRepository {
    Optional<Transportation> findById(String id);

    Transportation save(Transportation transportation);

    List<Transportation> findAllByOriginLocationAndDestinationLocationAndType(Location originLocation, Location destinationLocation, TransportationType transportationType);

    List<Transportation> findAllByOriginLocation(Location originLocation);

    void deleteById(String id);

    boolean existsByOriginLocationOrDestinationLocation(Location originLocation, Location destinationLocation);

    Page<Transportation> findAll(Pageable pageable);
}
