package com.kemalaydin.routemanagement.controller.route.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Route implements Serializable {
    private Integer order;
    private String type;
    private String from;
    private String to;
}
