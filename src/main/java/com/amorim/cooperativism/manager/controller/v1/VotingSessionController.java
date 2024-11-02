package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.constants.ApplicationConstants;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.API + ApplicationConstants.V1 + ApplicationConstants.VOTING_SESSION_ENDPOINT)
public class VotingSessionController {

    private final VotingSessionService service;


    public VotingSessionController(VotingSessionService service) {
        this.service = service;
    }

    @PutMapping("/{votingSession}/abrir")
    ResponseEntity<ApplicationResponse> open(@PathVariable("votingSession") Long votingSessionId) {
        return service.open(votingSessionId);
    }

    @PostMapping("/{votingSession}/votar")
    ResponseEntity<ApplicationResponse> vote(@PathVariable("votingSession") Long votingSessionId, @RequestBody @Valid VoteRequest request) {
        return service.vote(votingSessionId, request);
    }
}
