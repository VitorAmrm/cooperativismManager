package com.amorim.cooperativism.manager.service;

import com.amorim.cooperativism.manager.Application;
import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import org.springframework.http.ResponseEntity;

public interface VotingSessionService {

    ResponseEntity<ApplicationResponse> open(Long id);
    ResponseEntity<ApplicationResponse> create(VotingSessionRequest request, MeetingAgenda agenda);
    ResponseEntity<ApplicationResponse> vote(Long votingSessionId, VoteRequest request);

}
