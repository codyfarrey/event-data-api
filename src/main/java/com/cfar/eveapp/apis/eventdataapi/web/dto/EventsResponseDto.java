package com.cfar.eveapp.apis.eventdataapi.web.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class EventsResponseDto {
    private Collection<EventResponseDto> events;
}
