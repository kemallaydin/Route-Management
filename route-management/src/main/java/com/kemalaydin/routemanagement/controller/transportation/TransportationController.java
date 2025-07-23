package com.kemalaydin.routemanagement.controller.transportation;

import com.kemalaydin.routemanagement.controller.PagedResponse;
import com.kemalaydin.routemanagement.controller.transportation.request.CreateTransportationRequest;
import com.kemalaydin.routemanagement.controller.transportation.request.UpdateTransportationRequest;
import com.kemalaydin.routemanagement.controller.transportation.response.CreateTransportationResponse;
import com.kemalaydin.routemanagement.controller.transportation.response.GetTransportationResponse;
import com.kemalaydin.routemanagement.controller.transportation.response.UpdateTransportationResponse;
import com.kemalaydin.routemanagement.converter.TransportationConverter;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.service.TransportationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
@RequestMapping("transportations")
@Slf4j
public class TransportationController {
    private final TransportationService transportationService;
    private final TransportationConverter transportationConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(cacheNames = "routes", allEntries = true)
    public CreateTransportationResponse createTransportation(@RequestBody @Valid CreateTransportationRequest request) {
        log.info("[START] createTransportation - request={}", request);
        Transportation transportation = transportationConverter.createTransportationRequestToModel(request);
        Transportation createdTransportation = transportationService.createTransportation(transportation);
        CreateTransportationResponse response = transportationConverter.modelToCreateTransportationResponse(createdTransportation);
        log.info("[END] createTransportation - response={}", response);
        return response;
    }

    @PutMapping("/{id}")
    @CacheEvict(cacheNames = "routes", allEntries = true)
    public UpdateTransportationResponse updateTransportation(@PathVariable String id, @RequestBody @Valid UpdateTransportationRequest request) {
        log.info("[START] updateTransportation - id={}, request={}", id, request);
        Transportation transportation = transportationConverter.updateTransportationRequestToModel(request);
        Transportation updatedTransportation = transportationService.updateTransportation(id, transportation);
        UpdateTransportationResponse response = transportationConverter.modelToUpdateTransportationResponse(updatedTransportation);
        log.info("[END] updateTransportation - response={}", response);
        return response;
    }

    @GetMapping("/{id}")
    public GetTransportationResponse getTransportation(@PathVariable String id) {
        log.info("[START] getTransportation - id={}", id);
        Transportation transportation = transportationService.getTransportation(id);
        GetTransportationResponse response = transportationConverter.modelToGetTransportationResponse(transportation);
        log.info("[END] getTransportation - response={}", response);
        return response;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<GetTransportationResponse> getAllTransportation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        log.info("[START] getAllTransportation - page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        Page<Transportation> transportations = transportationService.getAllLocations(pageable);

        PagedResponse<GetTransportationResponse> response = transportationConverter.pageToPagedResponse(transportations);
        log.info("[END] getAllTransportation - response={}", response);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "routes", allEntries = true)
    public void deleteTransportation(@PathVariable String id) {
        log.info("[START] deleteTransportation - id={}", id);
        transportationService.deleteTransportation(id);
        log.info("[END] deleteTransportation - id={}", id);
    }
}
