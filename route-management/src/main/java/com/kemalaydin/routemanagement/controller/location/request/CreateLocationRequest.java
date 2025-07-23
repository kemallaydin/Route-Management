package com.kemalaydin.routemanagement.controller.location.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateLocationRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "Country must not be blank")
    private String country;
    @NotBlank(message = "City must not be blank")
    private String city;
    @NotBlank(message = "Code must not be blank")
    private String code;
}
