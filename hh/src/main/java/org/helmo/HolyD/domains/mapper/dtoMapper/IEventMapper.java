package org.helmo.HolyD.domains.mapper.dtoMapper;

import org.helmo.HolyD.domains.models.Event;
import org.helmo.HolyD.domains.requests.EventDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IScheduleMapper.class, IActivityMapper.class})
public interface IEventMapper {

    IEventMapper INSTANCE = Mappers.getMapper(IEventMapper.class);

    @Mapping(target = "scheduleDTO", source = "schedule")
    @Mapping(target = "activityDTO", source = "activity")
    EventDTO toEventDTO(Event event);

    @InheritInverseConfiguration
    Event toEvent(EventDTO event);
}
