package com.cfar.eveapp.apis.eventdataapi.service.impl;

import com.cfar.eveapp.apis.eventdataapi.entity.EventEntity;
import com.cfar.eveapp.apis.eventdataapi.repository.EventRepository;
import com.cfar.eveapp.apis.eventdataapi.service.EventService;
import com.cfar.eveapp.apis.eventdataapi.service.UserCacheService;
import com.cfar.eveapp.apis.eventdataapi.web.dto.CreateEventDto;
import com.cfar.eveapp.apis.eventdataapi.web.dto.EventResponseDto;
import com.cfar.eveapp.apis.eventdataapi.web.dto.EventsResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserCacheService userCacheService;

    @Override
    public EventResponseDto createEvent(CreateEventDto eventDto) {
        EventEntity eventEntity = modelMapper.map(eventDto, EventEntity.class);
        validateAuthorization(eventDto);

        EventEntity responseEntity = eventRepository.save(eventEntity);

        return modelMapper.map(responseEntity, EventResponseDto.class);
    }

    @Override
    public EventsResponseDto getAllEvents() {
        List<EventEntity> events = eventRepository.findAll();
        EventsResponseDto eventsResponse = new EventsResponseDto();

        List<EventResponseDto> eventDtoList = new ArrayList<>();

        for (EventEntity event : events) {
            EventResponseDto responseDto = modelMapper.map(event, EventResponseDto.class);
            eventDtoList.add(responseDto);
        }

        eventsResponse.setEvents(eventDtoList);

        return eventsResponse;
    }

    @Override
    public EventsResponseDto getEventsByUserId(long userId) {
        List<EventEntity> events = eventRepository.findByAuthorId(userId);
        EventsResponseDto eventsResponse = new EventsResponseDto();

        List<EventResponseDto> eventDtoList = new ArrayList<>();

        for (EventEntity event : events) {
            EventResponseDto responseDto = modelMapper.map(event, EventResponseDto.class);
            eventDtoList.add(responseDto);
        }

        eventsResponse.setEvents(eventDtoList);

        return eventsResponse;
    }

    @Override
    public EventResponseDto getEventById(long id) {
        return modelMapper.map(eventRepository.findById(id), EventResponseDto.class);
    }

    private void validateAuthorization(CreateEventDto eventDto) {
        if (eventDto.getAuthorId() != userCacheService.getUserDetails().getId()) {
            throw new RuntimeException("Not authorized");
        }
    }
}
