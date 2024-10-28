package org.helmo.HolyD.repositories;


import org.helmo.HolyD.domains.entities.UserEntity;
import org.helmo.HolyD.domains.entities.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<VacationEntity, Long> {

    Optional<VacationEntity> findByIdAndUserEntity_Id(Long id, Long userId);

    List<VacationEntity> findByUserEntity_Id(Long idUser);

    // Optional <VacationEntity> findByIdAndParticipantsContains(Long id, UserEntity userEntity);

   // Set<VacationEntity> findAllByParticipantsContains(UserEntity userEntity);
}
