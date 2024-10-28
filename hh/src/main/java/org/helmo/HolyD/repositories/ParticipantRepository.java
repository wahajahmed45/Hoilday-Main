package org.helmo.HolyD.repositories;

import org.helmo.HolyD.domains.entities.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
    Optional<ParticipantEntity> findByEmail(String email);

    //   int countDistinctByRoleIs(RoleDTO roleUser);

    // String oracleQuery = "SELECT v.lieu.pays as pays, COUNT(u.id) as nbrUserInHoliday FROM UserDTO u JOIN u.vacances v Where trunc(v.dateFin) >= trunc(?1) AND trunc(v.dateDebut) <= trunc(?1) GROUP BY v.lieu.pays";
    //String h2Query = "SELECT v.lieu.pays as pays, COUNT(u.id) as nbrUserInHoliday FROM UserDTO u JOIN u.vacances v Where trunc(v.dateFin + 1) > ?1 AND trunc(v.dateDebut) <= ?1 GROUP BY v.lieu.pays";
    //  @Query(value =  oracleQuery)
    //Collection<Tuple> countNbrOfUserInHolidayForADate(OffsetDateTime dateTime);
}