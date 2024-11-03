package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VotingSessionControllerTest {

    @InjectMocks
    private VotingSessionController votingSessionController;

    @Mock
    private VotingSessionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOpen() {
        Long votingSessionId = 1L;
        ApplicationResponse expectedResponse = new ApplicationResponse("",200);
        when(service.open(eq(votingSessionId))).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApplicationResponse> response = votingSessionController.open(votingSessionId);

        assertEquals(expectedResponse, response.getBody());
        verify(service).open(votingSessionId);
    }

    @Test
    void testVote() {
        Long votingSessionId = 1L;
        VoteRequest request = new VoteRequest();
        ApplicationResponse expectedResponse = new ApplicationResponse("",1);
        when(service.vote(eq(votingSessionId), any(VoteRequest.class))).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApplicationResponse> response = votingSessionController.vote(votingSessionId, request);

        assertEquals(expectedResponse, response.getBody());
        verify(service).vote(votingSessionId, request);
    }
}
