package org.helmo.HolyD.repositories;

import org.helmo.HolyD.domains.entities.EventEntity;
import org.helmo.HolyD.domains.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByScheduleEntity_Id(Long idSchedule);

    boolean existsByActivityEntity_Id(Long idActivity);

    Long findByActivityEntity_Id(Long idActivity);

    EventEntity findByActivityEntity_IdAndScheduleEntity_Id(Long idEvent, Long idActivity);

}