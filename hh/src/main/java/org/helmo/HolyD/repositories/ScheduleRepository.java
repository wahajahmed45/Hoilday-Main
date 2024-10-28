package org.helmo.HolyD.repositories;

import org.helmo.HolyD.domains.entities.PlaceEntity;
import org.helmo.HolyD.domains.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByUserEntity_Id(Long idUser);
}