package com.lanchonete.lanchon.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String code,
        List<ApiFieldError> fieldErrors
) {

    public ApiErrorResponse {
        fieldErrors = fieldErrors == null ? Collections.emptyList() : List.copyOf(fieldErrors);
    }

    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(Instant.now(), status, error, message, path, null, Collections.emptyList());
    }

    public static ApiErrorResponse of(int status, String error, String message, String path, String code) {
        return new ApiErrorResponse(Instant.now(), status, error, message, path, code, Collections.emptyList());
    }

    public static ApiErrorResponse ofValidation(int status, String error, String message, String path, List<ApiFieldError> fieldErrors) {
        return new ApiErrorResponse(Instant.now(), status, error, message, path, null, fieldErrors);
    }

    public ApiErrorResponse withCode(String value) {
        return new ApiErrorResponse(this.timestamp, this.status, this.error, this.message, this.path, value, this.fieldErrors);
    }
}
