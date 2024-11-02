package com.amorim.cooperativism.manager.service;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.to.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MeetingAgendaService {

    ResponseEntity<ApplicationResponse> create(MeetingAgendaRequest agendaRequest);
    ResponseEntity<ApplicationResponse> createVotingSession(VotingSessionRequest request, Long meetingAgendaId);
    ResponseEntity<ApplicationResponse> close(Long id);
    ResponseEntity<List<MeetingAgendaVO>> findAll();
    ResponseEntity<List<VotingSessionVO>> findAllSessionsByMeetingAgenda(Long meetingAgendaId);

}
