package com.example.SecurityFinal.Api.Exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


@Data
@Builder
@AllArgsConstructor
public class ApiException  {
    private ZonedDateTime zonedDateTime;
    private String message;
    private Throwable error;
    private HttpStatus status;
    private String path;

}