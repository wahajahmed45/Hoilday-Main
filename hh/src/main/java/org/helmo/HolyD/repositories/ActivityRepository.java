package org.helmo.HolyD.repositories;

import org.helmo.HolyD.domains.entities.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    List<ActivityEntity> findByUserEntity(Long idUser);

    Optional<ActivityEntity> findByIdAndUserEntity(Long idActivity, Long idUser);


     void deleteActivityEntitiesById(Long idActivity);
    //  Optional<ActivityEntity> findByIdAndParticipantsContains(Long id, UserEntity userEntity);
}
