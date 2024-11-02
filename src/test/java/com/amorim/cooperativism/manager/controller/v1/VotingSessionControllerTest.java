package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.constants.ApplicationConstants;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VotingSessionControllerTest {

    @InjectMocks
    private VotingSessionController controller;

    @Mock
    private VotingSessionService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOpenVotingSession() {
        Long votingSessionId = 1L;
        ApplicationResponse expectedResponse = new ApplicationResponse("Voting session opened", 200, new Date());

        when(service.open(votingSessionId)).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApplicationResponse> response = controller.open(votingSessionId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Voting session opened", response.getBody().getMessage());
        assertEquals(expectedResponse.getDatetime(), response.getBody().getDatetime());
        verify(service, times(1)).open(votingSessionId);
    }
}
