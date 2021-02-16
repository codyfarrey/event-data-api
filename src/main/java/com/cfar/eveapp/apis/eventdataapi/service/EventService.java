package com.cfar.eveapp.apis.eventdataapi.service;

import com.cfar.eveapp.apis.eventdataapi.web.dto.CreateEventDto;
import com.cfar.eveapp.apis.eventdataapi.web.dto.EventResponseDto;
import com.cfar.eveapp.apis.eventdataapi.web.dto.EventsResponseDto;

public interface EventService {
    EventResponseDto createEvent(CreateEventDto eventDto);

    EventResponseDto getEventById(long id);

    EventsResponseDto getAllEvents();

    EventsResponseDto getEventsByUserId(long userId);
}
