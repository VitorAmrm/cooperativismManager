package com.amorim.cooperativism.manager.service;

import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import org.springframework.http.ResponseEntity;

public interface MeetingAgendaService {

    ResponseEntity<ApplicationResponse> create(MeetingAgendaRequest agendaRequest);
    ResponseEntity<ApplicationResponse> createVotingSession(VotingSessionRequest request, Long meetingAgendaId);
    ResponseEntity<ApplicationResponse> close(Long id);

}
