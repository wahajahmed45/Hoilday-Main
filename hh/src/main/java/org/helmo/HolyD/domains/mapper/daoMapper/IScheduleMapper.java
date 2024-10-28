package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.ScheduleEntity;
import org.helmo.HolyD.domains.models.Schedule;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper()
public interface IScheduleMapper {

    IScheduleMapper INSTANCE = Mappers.getMapper(IScheduleMapper.class);

    @Mapping(target = "userEntity", source = "user")
    ScheduleEntity toScheduleEntity(Schedule schedule);

    @InheritInverseConfiguration
    Schedule toSchedule(ScheduleEntity scheduleEntity);
}
