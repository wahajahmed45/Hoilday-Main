package org.helmo.HolyD.repositories.dao.inter;

import org.helmo.HolyD.domains.models.Event;

import java.util.List;

public interface IEventDAO {

    Event getEvent(Long id);

    Event saveEvent(Event event);

    List<Event> getAllEvents();

    List<Event> getAllEventForUser(Long idSchedule);

    boolean isEventExists(Long idActivity);

    Long findEventByActivityId(Long idActivity);

    Long findScheduleByEventIdAndActivityId(Long idEvent, Long idActivity);

    void deleteEvent(Long idEvent);
}
