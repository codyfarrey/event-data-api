package com.cfar.eveapp.apis.eventdataapi.repository;

import com.cfar.eveapp.apis.eventdataapi.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByAuthorId(long authorId);
    EventEntity findById(long id);
}
