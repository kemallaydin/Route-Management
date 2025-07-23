package com.kemalaydin.routemanagement.controller.route;

import com.kemalaydin.routemanagement.controller.route.response.RoutesResponse;
import com.kemalaydin.routemanagement.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("routes")
@Slf4j
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENCY')")
    public RoutesResponse getRoutes(@RequestParam String originLocationCode,
                                    @RequestParam String destinationLocationCode,
                                    @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        log.info("[START] getRoutes - originLocationCode={}, destinationLocationCode={}, date={}",
                originLocationCode, destinationLocationCode, date);
        RoutesResponse response = routeService.getRoutes(originLocationCode, destinationLocationCode, date);
        log.info("[END] getRoutes - response={}", response);
        return response;
    }
}
