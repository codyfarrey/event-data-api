package com.cfar.eveapp.apis.eventdataapi.web;

import com.cfar.eveapp.apis.eventdataapi.service.EventService;
import com.cfar.eveapp.apis.eventdataapi.service.UserCacheService;
import com.cfar.eveapp.apis.eventdataapi.web.dto.CreateEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@RequestBody CreateEventDto createEventDto) {
        return ResponseEntity.ok().body(eventService.createEvent(createEventDto));
    }

    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok().body(eventService.getAllEvents());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEventById(@PathVariable(value="id") long id) {
        return ResponseEntity.ok().body(eventService.getEventById(id));
    }

    @GetMapping("/events/user/{id}")
    public ResponseEntity<?> getEventsByUserId(@PathVariable(value="id") long userId) {
        return ResponseEntity.ok().body(eventService.getEventsByUserId(userId));
    }
}
