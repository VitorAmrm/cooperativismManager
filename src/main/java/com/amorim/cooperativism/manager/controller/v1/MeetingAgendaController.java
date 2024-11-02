package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.constants.ApplicationConstants;
import com.amorim.cooperativism.manager.domain.to.*;
import com.amorim.cooperativism.manager.service.MeetingAgendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApplicationConstants.API + ApplicationConstants.V1 + ApplicationConstants.MEETING_AGENDA_ENDPOINT)
public class MeetingAgendaController {

    private final MeetingAgendaService service;

    public MeetingAgendaController(MeetingAgendaService service) {
        this.service = service;
    }

    @PostMapping("/")
    ResponseEntity<ApplicationResponse> create(@RequestBody @Valid MeetingAgendaRequest meetingAgendaRequest) {
        return service.create(meetingAgendaRequest);
    }

    @PostMapping("/{meetingAgenda}/sessao")
    ResponseEntity<ApplicationResponse> createSession(@RequestBody @Valid VotingSessionRequest votingSessionRequest, @PathVariable("meetingAgenda") Long meetingAgendaId) {
        return service.createVotingSession(votingSessionRequest, meetingAgendaId);
    }

    @PutMapping("/{meetingAgenda}/fechar")
    ResponseEntity<ApplicationResponse> close(@PathVariable("meetingAgenda") Long meetingAgendaId) {
        return service.close(meetingAgendaId);
    }

    @GetMapping("/")
    ResponseEntity<List<MeetingAgendaVO>> list(){
        return service.findAll();
    }

    @GetMapping("/{meetingAgenda}/sessao")
    ResponseEntity<List<VotingSessionVO>> listSessions(@PathVariable("meetingAgenda") Long meetingAgendaId){
        return service.findAllSessionsByMeetingAgenda(meetingAgendaId);
    }
}
