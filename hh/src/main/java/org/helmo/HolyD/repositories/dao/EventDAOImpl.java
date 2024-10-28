package org.helmo.HolyD.repositories.dao;

import jakarta.transaction.Transactional;
import org.helmo.HolyD.domains.entities.EventEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.IEventMapper;
import org.helmo.HolyD.domains.models.Event;
import org.helmo.HolyD.repositories.EventRepository;
import org.helmo.HolyD.repositories.dao.inter.IEventDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class EventDAOImpl implements IEventDAO {

    @Autowired
    private EventRepository eventRepository;
    private final static IEventMapper I_EVENT_MAPPER = IEventMapper.INSTANCE;

    @Override
    public Event getEvent(Long id) {
        Optional<EventEntity> eventEntity = eventRepository.findById(id);
        if (eventEntity.isPresent()) {
            EventEntity entity = eventEntity.get();
            Event event = I_EVENT_MAPPER.toEvent(entity);
            return event;
        }
        return null;
    }

    @Override
    public Event saveEvent(Event Event) {
        EventEntity eventEntity = I_EVENT_MAPPER.toEventEntity(Event);
        EventEntity savedEntity = eventRepository.save(eventEntity);
        Event savedEvent = I_EVENT_MAPPER.toEvent(savedEntity);
        return savedEvent;
    }

    @Override
    public List<Event> getAllEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<Event> events = eventEntities.stream().map(I_EVENT_MAPPER::toEvent).collect(Collectors.toList());
        return events;
    }

    @Override
    public List<Event> getAllEventForUser(Long idSchedule) {
        List<EventEntity> eventEntities = eventRepository.findByScheduleEntity_Id(idSchedule);
        List<Event> events = eventEntities.stream().map(I_EVENT_MAPPER::toEvent).collect(Collectors.toList());
        return events;
    }

    @Override
    public boolean isEventExists(Long idActivity) {
        return eventRepository.existsByActivityEntity_Id(idActivity);
    }

    @Override
    public Long findEventByActivityId(Long idActivity) {

        return eventRepository.findByActivityEntity_Id(idActivity);
    }

    @Override
    public Long findScheduleByEventIdAndActivityId(Long idEvent, Long idActivity) {
        return eventRepository.findByActivityEntity_IdAndScheduleEntity_Id(idEvent, idActivity).getScheduleEntity().getId();
    }

    @Override
    public void deleteEvent(Long idEvent) {
        eventRepository.deleteById(idEvent);
    }

  /*  @Override
    public Event createEventIfNotExist(Event Event) {
        Optional<EventEntity> EventEntity = EventRepository.findByCityAndCountryAndPostalCode(Event.getVille(),
                Event.getPays(), Event.getCodePostal());
        if (EventEntity.isEmpty()) {
            return saveEvent(Event);
        }
        return I_Event_MAPPER.toEvent(EventEntity.get());
    }*/

}
