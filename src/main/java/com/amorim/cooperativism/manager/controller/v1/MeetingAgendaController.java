package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.constants.ApplicationConstants;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.service.MeetingAgendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
