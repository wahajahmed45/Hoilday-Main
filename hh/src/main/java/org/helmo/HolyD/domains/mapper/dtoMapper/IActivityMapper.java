package org.helmo.HolyD.domains.mapper.dtoMapper;


import org.helmo.HolyD.domains.models.Activity;

import org.helmo.HolyD.domains.requests.ActivityDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(uses = {IPlaceMapper.class, IParticipantMapper.class})
public interface IActivityMapper {
    IActivityMapper INSTANCE = Mappers.getMapper(IActivityMapper.class);

    @Mapping(target = "place", source = "placeDTO")
    @Mapping(target = "participants", source = "participants")
    @Mapping(target = "vacation",source = "vacationDTO")
    Activity toActivity(ActivityDTO activityDTO);

    @InheritInverseConfiguration
    @Mapping(target = "placeDTO", source = "place")
    @Mapping(target = "participants", source = "participants")
    @Mapping(target = "vacationDTO",source = "vacation")
    ActivityDTO toActivityRequest(Activity activity);
}

