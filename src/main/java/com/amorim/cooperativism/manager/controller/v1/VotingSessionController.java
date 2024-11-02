package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.constants.ApplicationConstants;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
