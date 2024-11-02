package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.constants.ApplicationConstants;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.service.MeetingAgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MeetingAgendaControllerTest {

    @InjectMocks
    private MeetingAgendaController controller;

    @Mock
    private MeetingAgendaService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        // Arrange
        MeetingAgendaRequest request = new MeetingAgendaRequest();
        ApplicationResponse expectedResponse = new ApplicationResponse("Success", 200, new Date());

        when(service.create(request)).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApplicationResponse> response = controller.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(expectedResponse.getDatetime(), response.getBody().getDatetime());
        verify(service, times(1)).create(request);
    }

    @Test
    public void testCreateSession() {
        VotingSessionRequest request = new VotingSessionRequest();
        Long meetingAgendaId = 1L;
        ApplicationResponse expectedResponse = new ApplicationResponse("Session Created", 201, new Date());

        when(service.createVotingSession(request, meetingAgendaId)).thenReturn(ResponseEntity.status(201).body(expectedResponse));

        ResponseEntity<ApplicationResponse> response = controller.createSession(request, meetingAgendaId);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Session Created", response.getBody().getMessage());
        assertEquals(expectedResponse.getDatetime(), response.getBody().getDatetime());
        verify(service, times(1)).createVotingSession(request, meetingAgendaId);
    }

    @Test
    public void testClose() {
        // Arrange
        Long meetingAgendaId = 1L;
        ApplicationResponse expectedResponse = new ApplicationResponse("Meeting Agenda Closed", 200, new Date());


        when(service.close(meetingAgendaId)).thenReturn(ResponseEntity.ok(expectedResponse));

        // Act
        ResponseEntity<ApplicationResponse> response = controller.close(meetingAgendaId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Meeting Agenda Closed", response.getBody().getMessage());
        assertEquals(expectedResponse.getDatetime(), response.getBody().getDatetime());
        verify(service, times(1)).close(meetingAgendaId);
    }
}
