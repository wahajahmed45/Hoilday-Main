package org.helmo.HolyD.services.impl_service;

import jakarta.transaction.Transactional;
import org.helmo.HolyD.controllers.exception.IntervalActiviteIsNotInIntervalVacanceException;
import org.helmo.HolyD.controllers.exception.UserAlreadyInActiviteException;
import org.helmo.HolyD.controllers.exception.UserAlreadyInHolidayException;
import org.helmo.HolyD.domains.mapper.dtoMapper.*;
import org.helmo.HolyD.domains.models.*;
import org.helmo.HolyD.domains.requests.*;
import org.helmo.HolyD.domains.responses.MessageResponse;
import org.helmo.HolyD.repositories.dao.UserDAOImpl;
import org.helmo.HolyD.repositories.dao.inter.*;
import org.helmo.HolyD.securities.jwt.JwtUtils;
import org.helmo.HolyD.services.inter_service.IHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class HolidayServiceImpl implements IHolidayService {
    @Autowired
    private IVacationDAO vacationDAO;
    @Autowired
    private IActivityDAO activityDAO;
    @Autowired
    private IParticipantDAO participantDAO;
    @Autowired
    private IPlaceDAO placeDAO;
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private IScheduleDAO scheduleDAO;
    @Autowired
    private IEventDAO eventDAO;
    @Autowired
    private IDocumentDAO documentDAO;
    @Autowired
    private JwtUtils jwtUtils;
    private final static IVacationMapper I_VACATION_MAPPER = IVacationMapper.INSTANCE;
    private final static IActivityMapper I_ACTIVITY_MAPPER = IActivityMapper.INSTANCE;
    private final static IParticipantMapper I_PARTICIPANT_MAPPER = IParticipantMapper.INSTANCE;
    private final static IEventMapper I_EVENT_MAPPER = IEventMapper.INSTANCE;
    private final static IDocumentMapper I_DOCUMENT_MAPPER = IDocumentMapper.INSTANCE;

    @Override
    public VacationDTO createVacation(VacationDTO vacationDTO, String headerAuth) {
        Vacation vacation = I_VACATION_MAPPER.toVacation(vacationDTO);
        String username = jwtUtils.usernameByToken(headerAuth);
        User user = userDAO.findUserByEmail(username);
        if (user.userIsAlreadyInHoliday(vacationDTO.getStartDate(), vacationDTO.getEndDate())) {
            throw new UserAlreadyInHolidayException();
        }
        vacation.setOwner(user);
        Participant participant = participantDAO.createParticipantIfNoExist(user.getEmail(), user.getLastname(), user.getFirstname());
        vacation.initList();
        vacation.getParticipants().add(participant);
        Place placeFound = placeDAO.createPlaceIfNotExist(vacation.getPlace());
        vacation.setPlace(placeFound);
        vacation.setVacationStatus();
        Vacation saveVacation = vacationDAO.saveVacation(vacation);

        return I_VACATION_MAPPER.toVacationRequest(saveVacation);
    }


    @Override
    public List<VacationDTO> getAllVacations(String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        List<Vacation> vacationsFound = vacationDAO.getAllVacations(userFound.getId());
        return vacationsFound.stream().map(I_VACATION_MAPPER::toVacationRequest).collect(Collectors.toList());
    }

    @Override
    public VacationDTO getVacationById(Long id, String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        Vacation vacationFound = vacationDAO.getVacation(id);
        return I_VACATION_MAPPER.toVacationRequest(vacationFound);
    }

    @Override
    public ActivityDTO createActivity(Long id_vacation, ActivityDTO activityDTO, String headerAuth) {
        Activity activity = I_ACTIVITY_MAPPER.toActivity(activityDTO);
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        Vacation vacationFound = vacationDAO.findVacationByIdAndUserId(id_vacation, userFound.getId());

        if (!vacationFound.intervalIsInside(activityDTO.getDateDebut(), activityDTO.getDateFin()))
            throw new IntervalActiviteIsNotInIntervalVacanceException();
        else if (vacationFound.hasConflictWithExistingActivities(userFound,
                activityDTO.getDateDebut(),
                activityDTO.getDateFin()))
            throw new UserAlreadyInActiviteException();

        Participant participantFound = participantDAO.createParticipantIfNoExist(userFound.getEmail(), userFound.getLastname(), userFound.getFirstname());

        activity.getParticipants().add(participantFound);
        activity.setOwner(userFound.getId());
        activity.setVacation(vacationFound);
        Place placeFound = placeDAO.createPlaceIfNotExist(activity.getPlace());
        activity.setPlace(placeFound);
        activity.setActivityStatus();
        Activity saveActivity = activityDAO.saveActivity(activity);
        return I_ACTIVITY_MAPPER.toActivityRequest(saveActivity);
    }

    @Override
    public List<ActivityDTO> getAllActivities(String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        List<Activity> activities = activityDAO.getAllActivitiesForUser(userFound.getId());
        return activities.stream().map(I_ACTIVITY_MAPPER::toActivityRequest).collect(Collectors.toList());
    }

    @Override
    public ActivityDTO getActivityById(Long id_activity, String headerAuth) {
        // String username = jwtUtils.usernameByToken(headerAuth);
        // User userFound = userDAO.findUserByEmail(username);
        Activity activityFound = activityDAO.getActivity(id_activity);
        return I_ACTIVITY_MAPPER.toActivityRequest(activityFound);

    }

    @Override
    public ParticipantDTO createParticipantFromVacation(Long id_vacation, ParticipantDTO participantDTO, String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        Vacation vacationFound = vacationDAO.findVacationByIdAndUserId(id_vacation, userFound.getId());
        if (vacationFound.userIsAlreadyInHoliday()) {
            throw new UserAlreadyInHolidayException();
        }
        Participant participant = I_PARTICIPANT_MAPPER.toParticipant(participantDTO);
        participant = participantDAO.createParticipantIfNoExist(participant.getEmail(),
                participant.getNom(), participant.getPrenom());

        vacationFound.addParticipantFromVacation(participant);
        vacationDAO.saveVacation(vacationFound);

        return I_PARTICIPANT_MAPPER.toParticipantRequest(participant);
    }

    @Override
    public ActivityDTO updateActivity(Long id_vacation, Long idActivity, ActivityDTO activityDTO, String headerAuth) {
        Activity activity = I_ACTIVITY_MAPPER.toActivity(activityDTO);
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        Activity activityFound = activityDAO.getActivity(idActivity);
        Vacation vacationFound = vacationDAO.findVacationByIdAndUserId(id_vacation, userFound.getId());
        vacationFound.getActivities().remove(activityFound);
        if (!vacationFound.intervalIsInside(activityDTO.getDateDebut(), activityDTO.getDateFin()))
            throw new IntervalActiviteIsNotInIntervalVacanceException();
        else if (vacationFound.hasConflictWithExistingActivities(userFound,
                activityDTO.getDateDebut(), activityDTO.getDateFin()))
            throw new UserAlreadyInActiviteException();
        // Mise a jour du lieu
        Place placeFound = placeDAO.getPlace(activityFound.getPlace().getId());
        placeFound.setPlace(activity.getPlace());
        activityFound.setPlace(placeFound);
        placeDAO.savePlace(placeFound);
        activityFound.setActivity(activity);
        Activity saveActivity = activityDAO.saveActivity(activityFound);
        return I_ACTIVITY_MAPPER.toActivityRequest(saveActivity);
    }

    @Override
    public MessageResponse removeActivityFromVacation(Long idVacation, Long idActivity, String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User user = userDAO.findUserByEmail(username);
        //Activity activityFound = activityDAO.getActivity(idActivity);
        //Vacation vacationFound = vacationDAO.findVacationByIdAndUserId(idVacation, user.getId());
        //activityFound.removeAllParticipant();
        //Long idEvent = eventDAO.findEventByActivityId(idActivity);
        //   Long idSchedule = eventDAO.findScheduleByEventIdAndActivityId(idEvent,idActivity);
        //  eventDAO.deleteEvent(idEvent);
        //  scheduleDAO.deleteShedule(idSchedule);

        //vacationFound.removeActivity(activityFound);
        // activityFound = activityDAO.saveActivity(activityFound);
        // vacationFound = vacationDAO.saveVacation(vacationFound);
        // activityFound.setVacation(null);
        // activityFound = activityDAO.saveActivity(activityFound);
        //activityDAO.deleteActivity(activityFound);
        activityDAO.deleteActivityById(idActivity);
        //VacationDTO vrMapper = I_VACATION_MAPPER.toVacationRequest(vacationFound);
        return MessageResponse.builder().message("Activité Supprimer avec success").build();
    }

    @Override
    public MessageResponse removeParticipantFromActivity(Long idActivity, Long idParticipant, String headerAuth) {
        Activity activityFound = activityDAO.getActivity(idActivity);
        activityFound.removeParticipant(idParticipant);
        activityDAO.saveActivity(activityFound);
        // ActivityRequest activityRequestMapper = I_ACTIVITY_MAPPER.toActivityRequest(saveActivity);

        return MessageResponse.builder().message("Participant Supprimer avec success").build();
    }

    @Override
    public MessageResponse removeParticipantFromActivity(Long idParticipant) {
        participantDAO.deleteParticipant(idParticipant);
        return MessageResponse.builder().message("Participant Supprimer avec success").build();
    }

    @Override
    public MessageResponse removeParticipantFromVacation(Long idVacation, Long idParticipant) {
        Vacation vacationFound = vacationDAO.getVacation(idVacation);
        vacationFound.removeParticipant(idParticipant);
        vacationDAO.saveVacation(vacationFound);

        return MessageResponse.builder().message("Participant Supprimer avec success").build();
    }

    @Override
    public EventDTO addEventToScheduleFromActivity(Long idActivity, String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        return createEvent(idActivity, userFound);
    }

    private EventDTO createEvent(Long idActivity, User userFound) {
        Activity activityFound = activityDAO.findActivityByIdAndUserId(idActivity, userFound.getId());
        Event event = Event.builder().build();
        if (activityFound != null) {
            boolean isEventExist = eventDAO.isEventExists(idActivity);
            if (!isEventExist) {
                Schedule schedule = Schedule.builder()
                        .title(activityFound.getVacation().getName())
                        .user(userFound)
                        .build();
                schedule = scheduleDAO.saveSchedule(schedule);
                event = Event.builder()
                        .title(activityFound.getNom())
                        .activity(activityFound)
                        .description(activityFound.getDescription())
                        .startDate(activityFound.getDateDebut())
                        .endDate(activityFound.getDateFin())
                        .schedule(schedule)
                        .build();
                event = eventDAO.saveEvent(event);
            }
        }
        return I_EVENT_MAPPER.toEventDTO(event);
    }

    @Override
    public List<EventDTO> addEventToScheduleFromVacation(Long idVacation, String headerAuth) {
        List<EventDTO> eventDTOList = new ArrayList<>();
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        Vacation vacationFound = vacationDAO.findVacationByIdAndUserId(idVacation, userFound.getId());
        for (Activity activity : vacationFound.getActivities()) {
            EventDTO eventDTO = createEvent(activity.getId(), userFound);
            if (eventDTO.getId() != null) {
                eventDTOList.add(eventDTO);
            }
        }
        return eventDTOList;
    }

    @Override
    public List<EventDTO> getAllSchedules(String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        List<Schedule> eventForUser = scheduleDAO.getAllScheduleForUser(userFound.getId());
        List<Long> listIdSchedules = eventForUser.stream().map(Schedule::getId).collect(Collectors.toList());
        List<Event> eventList = new ArrayList<>();
        for (Long idSchedule : listIdSchedules) {
            eventList.addAll(eventDAO.getAllEventForUser(idSchedule));
        }
        return eventList.stream().map(I_EVENT_MAPPER::toEventDTO).collect(Collectors.toList());
    }

    @Override
    public MessageResponse createDocument(Long idVacation, DocumentDTO documentDTO, String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        Document document = I_DOCUMENT_MAPPER.toDocument(documentDTO);
        Vacation vacationFound = vacationDAO.findVacationByIdAndUserId(idVacation, userFound.getId());
        document.setVacation(vacationFound);
        document.setUser(userFound);
        documentDAO.create(document);
        return MessageResponse.builder().message("le document a bien été créer").build();
    }

    @Override
    public List<ParticipantDTO> getAllParticipants(String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        List<Vacation> vacationsFound = vacationDAO.getAllVacations(userFound.getId());
        List<ParticipantDTO> participantDTOList = new ArrayList<>();
        Set<Participant> participantList = new HashSet<>();
        for (Vacation vacation : vacationsFound) {
            participantList.addAll(vacation.getParticipants());
            for (Activity participant : vacation.getActivities()) {
                participantList.addAll(participant.getParticipants());
            }
        }
        for (Participant participant : participantList) {
            if(!Objects.equals(participant.getEmail(), userFound.getEmail())){
                participantDTOList.add(I_PARTICIPANT_MAPPER.toParticipantRequest(participant));
            }
        }
        return participantDTOList;
    }

    @Override
    public ParticipantDTO createParticipantFromActivity(Long idActivity, ParticipantDTO participantDTO, String headerAuth) {
        String username = jwtUtils.usernameByToken(headerAuth);
        User userFound = userDAO.findUserByEmail(username);
        Activity activityFound = activityDAO.findActivityByIdAndUserId(idActivity, userFound.getId());
        if (activityFound.userIsAlreadyInActivity())
            throw new UserAlreadyInActiviteException();
        Participant participant = I_PARTICIPANT_MAPPER.toParticipant(participantDTO);
        participant = participantDAO.createParticipantIfNoExist(participant.getEmail(), participant.getNom(), participant.getPrenom());
        activityFound.addParticipantFromActivity(participant);
        activityDAO.saveActivity(activityFound);
        return I_PARTICIPANT_MAPPER.toParticipantRequest(participant);
    }

}
