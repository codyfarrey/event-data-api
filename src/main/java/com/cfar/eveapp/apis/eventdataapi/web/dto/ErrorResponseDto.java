package com.cfar.eveapp.apis.eventdataapi.web.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    private int errorId;
    private String errorMessage;
    private String requestId;
}
