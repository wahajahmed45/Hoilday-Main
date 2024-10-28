package org.helmo.HolyD.repositories.dao;

import org.helmo.HolyD.domains.entities.ScheduleEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.IScheduleMapper;
import org.helmo.HolyD.domains.models.Schedule;
import org.helmo.HolyD.repositories.ScheduleRepository;
import org.helmo.HolyD.repositories.dao.inter.IScheduleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ScheduleDAOImpl implements IScheduleDAO {
    @Autowired
    private ScheduleRepository scheduleRepository;

    private final static IScheduleMapper I_Schedule_MAPPER = IScheduleMapper.INSTANCE;

    @Override
    public Schedule getSchedule(Long id) {
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(id);
        if (scheduleEntity.isPresent()) {
            ScheduleEntity entity = scheduleEntity.get();
            Schedule schedule = I_Schedule_MAPPER.toSchedule(entity);
            return schedule;
        }
        return null;
    }

    @Override
    public Schedule saveSchedule(Schedule Schedule) {
        ScheduleEntity scheduleEntity = I_Schedule_MAPPER.toScheduleEntity(Schedule);
        ScheduleEntity savedEntity = scheduleRepository.save(scheduleEntity);
        Schedule savedSchedule = I_Schedule_MAPPER.toSchedule(savedEntity);
        return savedSchedule;
    }

    @Override
    public List<Schedule> getAllSchedules() {
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findAll();
        List<Schedule> schedules = scheduleEntities.stream().map(I_Schedule_MAPPER::toSchedule).collect(Collectors.toList());
        return schedules;
    }

    @Override
    public List<Schedule> getAllScheduleForUser(Long id_user) {
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByUserEntity_Id(id_user);
        List<Schedule> schedules = scheduleEntities.stream().map(I_Schedule_MAPPER::toSchedule).collect(Collectors.toList());
        return schedules;
    }

    @Override
    public void deleteShedule(Long idSchedule) {
        scheduleRepository.deleteById(idSchedule);
    }

}
