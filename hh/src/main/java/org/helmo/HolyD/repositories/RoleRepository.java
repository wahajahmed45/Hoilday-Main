package org.helmo.HolyD.repositories;

import java.util.Optional;


import org.helmo.HolyD.domains.entities.RoleEntity;
import org.helmo.HolyD.domains.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    //Optional<RoleEntity> findByName(ERole name);
}