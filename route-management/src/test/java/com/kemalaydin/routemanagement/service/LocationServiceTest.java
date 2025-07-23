package com.kemalaydin.routemanagement.service;

import com.kemalaydin.routemanagement.exception.BadRequestException;
import com.kemalaydin.routemanagement.exception.NotFoundException;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.repository.location.LocationRepository;
import com.kemalaydin.routemanagement.repository.transportation.TransportationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @InjectMocks
    private LocationService underTest;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private TransportationRepository transportationRepository;

    private Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .id("1")
                .code("IST")
                .name("Istanbul Airport")
                .city("Istanbul")
                .country("Turkey")
                .build();
    }

    @Test
    void createLocation_success() {
        when(locationRepository.findFirstByCode(location.getCode())).thenReturn(Optional.empty());
        when(locationRepository.save(location)).thenReturn(location);

        Location created = underTest.createLocation(location);

        assertNotNull(created);
        assertEquals(location.getCode(), created.getCode());

        verify(locationRepository).findFirstByCode(location.getCode());
        verify(locationRepository).save(location);
    }

    @Test
    void createLocation_duplicateCode_throwsBadRequest() {
        when(locationRepository.findFirstByCode(location.getCode())).thenReturn(Optional.of(location));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> underTest.createLocation(location));

        assertEquals("Location with code IST already exists!", ex.getMessage());

        verify(locationRepository).findFirstByCode(location.getCode());
        verify(locationRepository, never()).save(any());
    }

    @Test
    void updateLocation_success() {
        List<Location> locationsByCode = List.of(location);

        Location requestedLocation = Location.builder()
                .code("IST")
                .name("Istanbul Airport Updated")
                .city("Istanbul")
                .country("Turkey")
                .build();

        when(locationRepository.findAllByCode(requestedLocation.getCode())).thenReturn(locationsByCode);
        when(locationRepository.save(location)).thenReturn(location);

        Location updated = underTest.updateLocation("1", requestedLocation);

        assertEquals("Istanbul Airport Updated", updated.getName());

        verify(locationRepository).findAllByCode(requestedLocation.getCode());
        verify(locationRepository).save(location);
    }

    @Test
    void updateLocation_idNotFound_throwsNotFound() {
        when(locationRepository.findAllByCode(location.getCode())).thenReturn(Collections.emptyList());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> underTest.updateLocation("1", location));

        assertEquals("Location with id 1 not found!", ex.getMessage());

        verify(locationRepository).findAllByCode(location.getCode());
        verify(locationRepository, never()).save(any());
    }

    @Test
    void updateLocation_duplicateCodeExists_throwsBadRequest() {
        Location otherLocation = Location.builder()
                .id("2")
                .code("IST")
                .build();

        List<Location> locationsByCode = List.of(location, otherLocation);

        when(locationRepository.findAllByCode(location.getCode())).thenReturn(locationsByCode);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> underTest.updateLocation("1", location));

        assertEquals("Another location with the same code exists!", ex.getMessage());

        verify(locationRepository).findAllByCode(location.getCode());
        verify(locationRepository, never()).save(any());
    }

    @Test
    void getLocation_success() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));

        Location locationResponse = underTest.getLocation(location.getId());

        assertNotNull(locationResponse);
        assertEquals(location.getCode(), locationResponse.getCode());

        verify(locationRepository).findById(location.getId());
    }

    @Test
    void getLocation_notFound_throwsNotFound() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> underTest.getLocation(location.getId()));

        assertEquals("Location with id 1 does not exist!", ex.getMessage());

        verify(locationRepository).findById(location.getId());
    }

    @Test
    void deleteLocation_success() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        when(transportationRepository.existsByOriginLocationOrDestinationLocation(location, location)).thenReturn(false);
        doNothing().when(locationRepository).deleteById(location.getId());

        assertDoesNotThrow(() -> underTest.deleteLocation(location.getId()));

        verify(locationRepository).findById(location.getId());
        verify(transportationRepository).existsByOriginLocationOrDestinationLocation(location, location);
        verify(locationRepository).deleteById(location.getId());
    }

    @Test
    void deleteLocation_inUse_throwsBadRequest() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        when(transportationRepository.existsByOriginLocationOrDestinationLocation(location, location)).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> underTest.deleteLocation(location.getId()));

        assertEquals("Cannot delete location because it is used in transportation!", ex.getMessage());

        verify(locationRepository).findById(location.getId());
        verify(transportationRepository).existsByOriginLocationOrDestinationLocation(location, location);
        verify(locationRepository, never()).deleteById(any());
    }

    @Test
    void getAllLocations_success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Location> locations = List.of(location);
        Page<Location> page = new PageImpl<>(locations, pageable, locations.size());

        when(locationRepository.findAll(pageable)).thenReturn(page);

        Page<Location> result = underTest.getAllLocations(pageable);

        assertEquals(1, result.getTotalElements());
        verify(locationRepository).findAll(pageable);
    }
}

