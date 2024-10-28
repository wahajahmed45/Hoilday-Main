package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.ParticipantEntity;
import org.helmo.HolyD.domains.models.Participant;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IParticipantMapper {
    IParticipantMapper INSTANCE = Mappers.getMapper(IParticipantMapper.class);

    @Mapping(target = "lastname", source = "nom")
    @Mapping(target = "firstname", source = "prenom")
    @Mapping(target = "email", source = "email")
    ParticipantEntity toParticipantEntity(Participant participant);

    @InheritInverseConfiguration
    @Mapping(target = "nom", source = "lastname")
    @Mapping(target = "prenom", source = "firstname")
    @Mapping(target = "email", source = "email")
    Participant toParticipant(ParticipantEntity participant);

}
