package org.helmo.HolyD.domains.mapper.dtoMapper;

import org.helmo.HolyD.domains.models.Schedule;
import org.helmo.HolyD.domains.requests.ScheduleDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface IScheduleMapper {

    IScheduleMapper INSTANCE = Mappers.getMapper(IScheduleMapper.class);

    ScheduleDTO toScheduleDTO(Schedule schedule);

    @InheritInverseConfiguration
    Schedule toSchedule(ScheduleDTO scheduleDTO);
}
