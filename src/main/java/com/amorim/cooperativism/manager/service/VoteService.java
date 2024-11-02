package com.amorim.cooperativism.manager.service;

import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import org.springframework.http.ResponseEntity;

public interface VoteService {
    ResponseEntity<ApplicationResponse> vote(VoteRequest request, VotingSession session);
}
