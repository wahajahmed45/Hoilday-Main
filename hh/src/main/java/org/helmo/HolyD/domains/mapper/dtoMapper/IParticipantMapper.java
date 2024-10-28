package org.helmo.HolyD.domains.mapper.dtoMapper;

import org.helmo.HolyD.domains.models.Participant;
import org.helmo.HolyD.domains.requests.ParticipantDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IParticipantMapper {
    IParticipantMapper INSTANCE = Mappers.getMapper(IParticipantMapper.class);


    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    @Mapping(target = "email", source = "email")
    Participant toParticipant(ParticipantDTO participantDTO);

    @InheritInverseConfiguration
    @Mapping(target = "prenom", source = "nom")
    @Mapping(target = "nom", source = "prenom")
    @Mapping(target = "email", source = "email")
    ParticipantDTO toParticipantRequest(Participant participant);


}
