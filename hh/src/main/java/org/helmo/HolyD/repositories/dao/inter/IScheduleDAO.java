package org.helmo.HolyD.repositories.dao.inter;

import org.helmo.HolyD.domains.models.Place;
import org.helmo.HolyD.domains.models.Schedule;

import java.util.List;

public interface IScheduleDAO {
    Schedule getSchedule(Long id);

    Schedule saveSchedule(Schedule schedule);

    List<Schedule> getAllSchedules();

    List<Schedule> getAllScheduleForUser(Long id);

    void deleteShedule(Long idSchedule);

    // Schedule createPlaceIfNotExist(Schedule schedule);
}
