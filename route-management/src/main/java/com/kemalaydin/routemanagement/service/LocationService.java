package com.kemalaydin.routemanagement.service;

import com.kemalaydin.routemanagement.exception.BadRequestException;
import com.kemalaydin.routemanagement.exception.NotFoundException;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.repository.location.LocationRepository;
import com.kemalaydin.routemanagement.repository.transportation.TransportationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    private final TransportationRepository transportationRepository;

    public Location createLocation(Location location) {
        Optional<Location> optionalLocationByCode = locationRepository.findFirstByCode(location.getCode());

        if (optionalLocationByCode.isPresent()) {
            throw new BadRequestException(String.format("Location with code %s already exists!", location.getCode()));
        }

        return locationRepository.save(location);
    }

    public Location updateLocation(String id, Location location) {
        List<Location> locationsByCode = locationRepository.findAllByCode(location.getCode());

        Location locationById = locationsByCode.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Location with id %s not found!", id)));

        boolean duplicateExists = locationsByCode.stream().anyMatch(l -> !l.getId().equals(id));

        if (duplicateExists) {
            throw new BadRequestException("Another location with the same code exists!");
        }

        locationById.setCode(location.getCode());
        locationById.setName(location.getName());
        locationById.setCity(location.getCity());
        locationById.setCountry(location.getCountry());

        return locationRepository.save(locationById);
    }

    public Location getLocation(String id) {
        return getLocationById(id);
    }

    private Location getLocationById(String id) {
        Optional<Location> optionalLocationById = locationRepository.findById(id);

        if (optionalLocationById.isEmpty()) {
            throw new NotFoundException(String.format("Location with id %s does not exist!", id));
        }

        return optionalLocationById.get();
    }

    public void deleteLocation(String id) {
        Location locationById = getLocationById(id);

        boolean used = transportationRepository.existsByOriginLocationOrDestinationLocation(locationById, locationById);

        if (used) {
            throw new BadRequestException("Cannot delete location because it is used in transportation!");
        }

        locationRepository.deleteById(id);
    }

    public Page<Location> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }
}
