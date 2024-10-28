package org.helmo.HolyD.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.helmo.HolyD.domains.requests.*;
import org.helmo.HolyD.domains.responses.MessageResponse;
import org.helmo.HolyD.services.inter_service.IHolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/vacations")
public class HolidayController {

    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    @Autowired
    private IHolidayService holidayService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacationDTO> createVacation(@Valid @RequestBody VacationDTO vacationDTO, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        VacationDTO vacationCreated = holidayService.createVacation(vacationDTO, headerAuth);
        logger.info("Vacation created: {}", vacationCreated);
        return ResponseEntity.ok(vacationCreated);
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/{id_vacation}/participant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParticipantDTO> createParticipant(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation, @Valid @RequestBody ParticipantDTO participantDTO, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        ParticipantDTO vacationCreated = holidayService.createParticipantFromVacation(id_vacation, participantDTO, headerAuth);
        logger.info("Participant created: {}", vacationCreated);
        return ResponseEntity.ok(vacationCreated);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id_vacation}")
    public ResponseEntity<VacationDTO> getVacation(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        VacationDTO vacationRequests = holidayService.getVacationById(id_vacation, headerAuth);
        logger.info("Vacation Found: {}", vacationRequests);
        return ResponseEntity.ok(vacationRequests);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public ResponseEntity<List<VacationDTO>> getAllVacations(HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        List<VacationDTO> vacationDTOS = holidayService.getAllVacations(headerAuth);
        logger.info("All Vacations Found: {}", vacationDTOS);
        return ResponseEntity.ok(vacationDTOS);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/{id_vacation}/activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> createActivity(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation, @Valid @RequestBody ActivityDTO activityDTO, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        ActivityDTO activityCreated = holidayService.createActivity(id_vacation, activityDTO, headerAuth);
        logger.info("Activity created: {}", activityCreated);
        return ResponseEntity.ok(activityCreated);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/activities")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        List<ActivityDTO> allActivities = holidayService.getAllActivities(headerAuth);
        logger.info("All Activities Found: {}", allActivities);
        return ResponseEntity.ok(allActivities);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/activity/{id_activity}")
    public ResponseEntity<ActivityDTO> getActivity(@Valid @Min(1) @PathVariable("id_activity") Long id_activity, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        ActivityDTO activityDTO = holidayService.getActivityById(id_activity, headerAuth);
        logger.info("Activity Found: {}", activityDTO);
        return ResponseEntity.ok(activityDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id_vacation}/activity/{id_activity}")
    public ResponseEntity<ActivityDTO> updateActivity(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation,
                                                      @Valid @Min(1) @PathVariable("id_activity") Long id_activity,
                                                      @Valid @RequestBody ActivityDTO activityDTO, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        ActivityDTO activityUpdated = holidayService.updateActivity(id_vacation, id_activity, activityDTO, headerAuth);
        logger.info("Activity Updated: {}", activityUpdated);
        return ResponseEntity.ok(activityUpdated);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "{id_vacation}/activity/{id_activity}")
    public ResponseEntity<MessageResponse> deleteActivityFromVacation(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation,
                                                                      @Valid @Min(1) @PathVariable("id_activity") Long id_activity,
                                                                      HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        MessageResponse activityDelete = holidayService.removeActivityFromVacation(id_vacation, id_activity, headerAuth);
        logger.info("Activity Deleted: {}", activityDelete);
        return ResponseEntity.ok(activityDelete);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id_vacation}/participant/{id_participant}")
    public ResponseEntity<MessageResponse> deleteParticipantFromVacation(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation,
                                                                         @Valid @Min(1) @PathVariable("id_participant") Long id_participant,
                                                                         HttpServletRequest servletRequest) {
        //String headerAuth = servletRequest.getHeader("Authorization");
        MessageResponse activityDelete = holidayService.removeParticipantFromVacation(id_vacation, id_participant);
        logger.info("Participant for Activity Deleted: {}", activityDelete);
        return ResponseEntity.ok(activityDelete);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/activity/{id_activity}/participant/{id_participant}")
    public ResponseEntity<MessageResponse> deleteParticipantFromActivity(@Valid @Min(1) @PathVariable("id_activity") Long id_activity, @Valid @Min(1) @PathVariable("id_participant") Long id_participant, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        MessageResponse activityDelete = holidayService.removeParticipantFromActivity(id_activity, id_participant, headerAuth);
        logger.info("Participant Deleted from Activity : {}", activityDelete);
        return ResponseEntity.ok(activityDelete);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/activity/{id_activity}/participant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParticipantDTO> createParticipantForActivity(@Valid @Min(1) @PathVariable("id_activity") Long id_activity,
                                                                       @Valid @RequestBody ParticipantDTO participantDTO, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        ParticipantDTO participantCreated = holidayService.createParticipantFromActivity(id_activity, participantDTO, headerAuth);
        logger.info("Participant For Activity created : {}", participantCreated);
        return ResponseEntity.ok(participantCreated);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/schedule/activity/{id_activity}")
    public ResponseEntity<EventDTO> getScheduleFromActivity(@Valid @Min(1) @PathVariable("id_activity") Long id_activity, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        EventDTO eventDTO = holidayService.addEventToScheduleFromActivity(id_activity, headerAuth);
        logger.info("Event added to schedule : {}", eventDTO);
        return ResponseEntity.ok(eventDTO);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/schedule/vacation/{id_vacation}")
    public ResponseEntity<List<EventDTO>> getScheduleFromVacation(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        List<EventDTO> eventDTO = holidayService.addEventToScheduleFromVacation(id_vacation, headerAuth);
        logger.info("All Event added to schedule : {}", eventDTO);
        return ResponseEntity.ok(eventDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/schedule/event")
    public ResponseEntity<List<EventDTO>> getAllSchedules(HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        List<EventDTO> eventDTO = holidayService.getAllSchedules(headerAuth);
        logger.info("All schedule loaded: {}", eventDTO);
        return ResponseEntity.ok(eventDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/document/{id_vacation}")
    public ResponseEntity<MessageResponse> createDocuments(@Valid @Min(1) @PathVariable("id_vacation") Long id_vacation,
                                                           @RequestBody DocumentDTO documentDTO, HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        MessageResponse messageResponse = holidayService.createDocument(id_vacation, documentDTO, headerAuth);
        logger.info("All Document Found: {}", messageResponse);
        return ResponseEntity.ok(messageResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/participants")
    public ResponseEntity<List<ParticipantDTO>> getParticipants(HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader("Authorization");
        List<ParticipantDTO> participantDTOS = holidayService.getAllParticipants(headerAuth);
        logger.info("All participants loaded: {}", participantDTOS);
        return ResponseEntity.ok(participantDTOS);
    }
}
