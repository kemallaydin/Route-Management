package com.kemalaydin.routemanagement.service;

import com.kemalaydin.routemanagement.exception.BadRequestException;
import com.kemalaydin.routemanagement.exception.NotFoundException;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.model.transportation.TransportationType;
import com.kemalaydin.routemanagement.repository.location.LocationRepository;
import com.kemalaydin.routemanagement.repository.transportation.TransportationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransportationService {
    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;

    public Transportation createTransportation(Transportation transportation) {
        validateDifferentLocations(transportation);

        Location originLocation = getLocationByCode(transportation.getOriginLocation().getCode());
        Location destinationLocation = getLocationByCode(transportation.getDestinationLocation().getCode());

        transportation.setOriginLocation(originLocation);
        transportation.setDestinationLocation(destinationLocation);

        ensureNoDuplicateTransportation(null, originLocation, destinationLocation, transportation.getType());

        return transportationRepository.save(transportation);
    }

    public Transportation updateTransportation(String id, Transportation transportation) {
        validateDifferentLocations(transportation);

        Transportation existingTransportation = getTransportationById(id);

        Location originLocation = getLocationByCode(transportation.getOriginLocation().getCode());
        Location destinationLocation = getLocationByCode(transportation.getDestinationLocation().getCode());

        ensureNoDuplicateTransportation(id, originLocation, destinationLocation, transportation.getType());

        existingTransportation.setOriginLocation(originLocation);
        existingTransportation.setDestinationLocation(destinationLocation);
        existingTransportation.setType(transportation.getType());
        existingTransportation.setOperatingDays(transportation.getOperatingDays());

        return transportationRepository.save(existingTransportation);
    }

    private void validateDifferentLocations(Transportation transportation) {
        if (transportation.getOriginLocation().getCode().equals(transportation.getDestinationLocation().getCode())) {
            throw new BadRequestException("Origin and destination locations cannot be same!");
        }
    }

    private Location getLocationByCode(String code) {
        return locationRepository.findFirstByCode(code)
                .orElseThrow(() -> new NotFoundException(String.format("Location not found: %s", code)));
    }

    private Transportation getTransportationById(String id) {
        return transportationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Transportation with id %s does not exist!", id)));
    }

    private void ensureNoDuplicateTransportation(String currentTransportationId, Location origin, Location destination, TransportationType type) {
        boolean duplicateExists = transportationRepository
                .findAllByOriginLocationAndDestinationLocationAndType(origin, destination, type)
                .stream()
                .anyMatch(t -> !t.getId().equals(currentTransportationId));

        if (duplicateExists) {
            throw new BadRequestException("Transportation with the same origin, destination location and type already exists!");
        }
    }

    public Transportation getTransportation(String id) {
        return getTransportationById(id);
    }

    public void deleteTransportation(String id) {
        transportationRepository.deleteById(id);
    }

    public Page<Transportation> getAllLocations(Pageable pageable) {
        return transportationRepository.findAll(pageable);
    }
}
