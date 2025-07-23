package com.kemalaydin.routemanagement.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private String timestamp;
    private String traceId;
}
