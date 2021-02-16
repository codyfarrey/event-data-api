package com.cfar.eveapp.apis.eventdataapi.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class EventResponseDto {
    private long id;
    private long authorId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String image;
    private Collection<String> keyWords;
}
