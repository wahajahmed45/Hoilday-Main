package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.ActivityEntity;
import org.helmo.HolyD.domains.models.Activity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PlaceMapper.class, IParticipantMapper.class})
public interface IActivityMapper {
    IActivityMapper INSTANCE = Mappers.getMapper(IActivityMapper.class);

    @Mapping(target = "startDate", source = "dateDebut")
    @Mapping(target = "endDate", source = "dateFin")
    @Mapping(target = "name", source = "nom")
    @Mapping(target = "userEntity", source = "owner")
    @Mapping(target = "placeEntity", source = "place")
    @Mapping(target = "vacationEntity", source = "vacation")
    @Mapping(target = "participantEntities", source = "participants")
    @Mapping(target = "status", source = "status")
    ActivityEntity toActivityEntity(Activity activity);

    @InheritInverseConfiguration
    Activity toActivity(ActivityEntity activityEntity);
}
