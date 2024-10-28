package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.EventEntity;
import org.helmo.HolyD.domains.models.Event;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IScheduleMapper.class, IActivityMapper.class})
public interface IEventMapper {
    IEventMapper INSTANCE = Mappers.getMapper(IEventMapper.class);

    @Mapping(target = "scheduleEntity", source = "schedule")
    @Mapping(target = "activityEntity", source = "activity")
    EventEntity toEventEntity(Event event);

    @InheritInverseConfiguration
    Event toEvent(EventEntity eventEntity);
}
