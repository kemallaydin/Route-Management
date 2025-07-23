package com.kemalaydin.routemanagement.model.location;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Location {
    private String id;
    private String name;
    private String country;
    private String city;
    private String code;
}
