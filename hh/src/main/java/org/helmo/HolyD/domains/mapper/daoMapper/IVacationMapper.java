package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.VacationEntity;
import org.helmo.HolyD.domains.models.Vacation;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PlaceMapper.class, IParticipantMapper.class, IActivityMapper.class,
        IDocumentMapper.class})
public interface IVacationMapper {
    IVacationMapper INSTANCE = Mappers.getMapper(IVacationMapper.class);

    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "userEntity", source = "owner")
    @Mapping(target = "placeEntity", source = "place")
    @Mapping(target = "participantEntities", source = "participants")
    @Mapping(target = "activityEntities", source = "activities")
    @Mapping(target = "documentEntities", source = "documents")
    @Mapping(target = "name", source = "name")
    VacationEntity toVacationEntity(Vacation vacation);

    @InheritInverseConfiguration
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "owner", source = "userEntity")
    @Mapping(target = "place", source = "placeEntity")
    @Mapping(target = "participants", source = "participantEntities")
    @Mapping(target = "activities", source = "activityEntities")
    @Mapping(target = "documents", source = "documentEntities")
    @Mapping(target = "name", source = "name")
    Vacation toVacation(VacationEntity vacationEntity);
}
