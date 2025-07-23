package com.kemalaydin.routemanagement.controller.location;

import com.kemalaydin.routemanagement.controller.PagedResponse;
import com.kemalaydin.routemanagement.controller.location.request.CreateLocationRequest;
import com.kemalaydin.routemanagement.controller.location.request.UpdateLocationRequest;
import com.kemalaydin.routemanagement.controller.location.response.CreateLocationResponse;
import com.kemalaydin.routemanagement.controller.location.response.GetLocationResponse;
import com.kemalaydin.routemanagement.controller.location.response.UpdateLocationResponse;
import com.kemalaydin.routemanagement.converter.LocationConverter;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("locations")
@Slf4j
public class LocationController {
    private final LocationService locationService;
    private final LocationConverter locationConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CreateLocationResponse createLocation(@RequestBody @Valid CreateLocationRequest request) {
        log.info("[START] createLocation - request={}", request);
        Location location = locationConverter.createLocationRequestToModel(request);
        Location createdLocation = locationService.createLocation(location);
        CreateLocationResponse response = locationConverter.modelToCreateLocationResponse(createdLocation);
        log.info("[END] createLocation - response={}", response);
        return response;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UpdateLocationResponse upsertLocation(@PathVariable String id, @RequestBody @Valid UpdateLocationRequest request) {
        log.info("[START] upsertLocation - id={}, request={}", id, request);
        Location location = locationConverter.upsertLocationRequestToModel(request);
        Location upsertedLocation = locationService.updateLocation(id, location);
        UpdateLocationResponse response = locationConverter.modelToUpsertLocationResponse(upsertedLocation);
        log.info("[END] upsertLocation - response={}", response);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENCY')")
    public GetLocationResponse getLocation(@PathVariable String id) {
        log.info("[START] getLocation - id={}", id);
        Location location = locationService.getLocation(id);
        GetLocationResponse response = locationConverter.modelToGetLocationResponse(location);
        log.info("[END] getLocation - response={}", response);
        return response;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENCY')")
    public PagedResponse<GetLocationResponse> getAllLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        log.info("[START] getAllLocations - page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        Page<Location> locations = locationService.getAllLocations(pageable);

        PagedResponse<GetLocationResponse> response = locationConverter.pageToPagedResponse(locations);
        log.info("[END] getAllLocations - response={}", response);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLocation(@PathVariable String id) {
        log.info("[START] deleteLocation - id={}", id);
        locationService.deleteLocation(id);
        log.info("[END] deleteLocation - id={}", id);
    }
}
