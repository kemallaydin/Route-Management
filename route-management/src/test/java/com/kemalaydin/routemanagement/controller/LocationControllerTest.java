package com.kemalaydin.routemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kemalaydin.routemanagement.controller.location.LocationController;
import com.kemalaydin.routemanagement.controller.location.request.CreateLocationRequest;
import com.kemalaydin.routemanagement.controller.location.request.UpdateLocationRequest;
import com.kemalaydin.routemanagement.controller.location.response.CreateLocationResponse;
import com.kemalaydin.routemanagement.controller.location.response.GetLocationResponse;
import com.kemalaydin.routemanagement.controller.location.response.UpdateLocationResponse;
import com.kemalaydin.routemanagement.converter.LocationConverter;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {
    @InjectMocks
    protected LocationController underTest;
    @Mock
    private LocationService locationService;
    @Mock
    private LocationConverter locationConverter;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
    }

    @Test
    void createLocation_returnsCreatedResponse() throws Exception {
        CreateLocationRequest request = CreateLocationRequest.builder()
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Location location = Location.builder()
                .id("1")
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Location createdLocation = Location.builder()
                .id("1")
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        CreateLocationResponse response = CreateLocationResponse.builder()
                .id("1")
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Mockito.when(locationConverter.createLocationRequestToModel(any())).thenReturn(location);
        Mockito.when(locationService.createLocation(location)).thenReturn(createdLocation);
        Mockito.when(locationConverter.modelToCreateLocationResponse(createdLocation)).thenReturn(response);

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Mockito.verify(locationService, Mockito.times(1)).createLocation(location);
    }

    @Test
    void upsertLocation_returnsUpdatedResponse() throws Exception {
        String locationId = "1";

        UpdateLocationRequest request = UpdateLocationRequest.builder()
                .name("Istanbul Airport Updated")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Location location = Location.builder()
                .id(locationId)
                .name("Istanbul Airport Updated")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Location updatedLocation = Location.builder()
                .id(locationId)
                .name("Istanbul Airport Updated")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        UpdateLocationResponse response = UpdateLocationResponse.builder()
                .id(locationId)
                .name("Istanbul Airport Updated")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Mockito.when(locationConverter.upsertLocationRequestToModel(any())).thenReturn(location);
        Mockito.when(locationService.updateLocation(locationId, location)).thenReturn(updatedLocation);
        Mockito.when(locationConverter.modelToUpsertLocationResponse(updatedLocation)).thenReturn(response);

        mockMvc.perform(put("/locations/{id}", locationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(locationService, Mockito.times(1)).updateLocation(locationId, location);
    }

    @Test
    void getLocation_returnsLocationResponse() throws Exception {
        String locationId = "1";

        Location location = Location.builder()
                .id(locationId)
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        GetLocationResponse response = GetLocationResponse.builder()
                .id(locationId)
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Mockito.when(locationService.getLocation(locationId)).thenReturn(location);
        Mockito.when(locationConverter.modelToGetLocationResponse(location)).thenReturn(response);

        mockMvc.perform(get("/locations/{id}", locationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(locationService, Mockito.times(1)).getLocation(locationId);
    }

    @Test
    void getAllLocations_returnsPagedResponse() throws Exception {
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        Location location1 = Location.builder()
                .id("1")
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        Location location2 = Location.builder()
                .id("2")
                .name("Sabiha Gökçen Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("SAW")
                .build();

        List<Location> locationList = List.of(location1, location2);
        Page<Location> locationPage = new PageImpl<>(locationList, pageable, locationList.size());

        GetLocationResponse response1 = GetLocationResponse.builder()
                .id("1")
                .name("Istanbul Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("IST")
                .build();

        GetLocationResponse response2 = GetLocationResponse.builder()
                .id("2")
                .name("Sabiha Gökçen Airport")
                .country("Türkiye")
                .city("İstanbul")
                .code("SAW")
                .build();

        Mockito.when(locationService.getAllLocations(pageable)).thenReturn(locationPage);
        Mockito.when(locationConverter.modelToGetLocationResponse(location1)).thenReturn(response1);
        Mockito.when(locationConverter.modelToGetLocationResponse(location2)).thenReturn(response2);

        mockMvc.perform(get("/locations")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(locationService, Mockito.times(1)).getAllLocations(pageable);
    }

    @Test
    void deleteLocation_returnsNoContent() throws Exception {
        String locationId = "1";

        Mockito.doNothing().when(locationService).deleteLocation(locationId);

        mockMvc.perform(delete("/locations/{id}", locationId))
                .andExpect(status().isNoContent());

        Mockito.verify(locationService, Mockito.times(1)).deleteLocation(locationId);
    }
}
