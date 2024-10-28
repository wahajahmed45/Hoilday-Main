package org.helmo.HolyD.services.inter_service;

import org.helmo.HolyD.domains.requests.*;
import org.helmo.HolyD.domains.responses.MessageResponse;

import java.util.List;

public interface IHolidayService {

    VacationDTO createVacation(VacationDTO vacationDTO, String headerAuth);

    List<VacationDTO> getAllVacations(String headerAuth);

    VacationDTO getVacationById(Long id, String headerAuth);

    ActivityDTO createActivity(Long id_vacation, ActivityDTO activityDTO, String headerAuth);

    List<ActivityDTO> getAllActivities(String headerAuth);

    ActivityDTO getActivityById(Long id, String headerAuth);

    ParticipantDTO createParticipantFromVacation(Long id_vacation, ParticipantDTO participantDTO, String headerAuth);

    ParticipantDTO createParticipantFromActivity(Long idActivity, ParticipantDTO participantDTO, String headerAuth);

    ActivityDTO updateActivity(Long id_vacation, Long idActivity, ActivityDTO activityDTO, String headerAuth);

    MessageResponse removeActivityFromVacation(Long idVacation, Long idActivity, String headerAuth);

    MessageResponse removeParticipantFromActivity(Long idActivity, Long idParticipant, String headerAuth);

    MessageResponse removeParticipantFromActivity(Long idParticipant);

    MessageResponse removeParticipantFromVacation(Long idVacation, Long idParticipant);

    EventDTO addEventToScheduleFromActivity(Long idActivity, String headerAuth);

    List<EventDTO> addEventToScheduleFromVacation(Long idVacation, String headerAuth);

    List<EventDTO> getAllSchedules(String headerAuth);

    MessageResponse createDocument(Long idVacation, DocumentDTO documentDTO, String headerAuth);

    List<ParticipantDTO> getAllParticipants(String headerAuth);
}
