package com.amorim.cooperativism.manager.controller.v1;

import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaRequest;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaVO;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionVO;
import com.amorim.cooperativism.manager.service.MeetingAgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MeetingAgendaControllerTest {

    @InjectMocks
    private MeetingAgendaController meetingAgendaController;

    @Mock
    private MeetingAgendaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        MeetingAgendaRequest request = new MeetingAgendaRequest();
        ApplicationResponse expectedResponse = new ApplicationResponse("",1);
        when(service.create(any(MeetingAgendaRequest.class))).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApplicationResponse> response = meetingAgendaController.create(request);

        assertEquals(expectedResponse, response.getBody());
        verify(service).create(request);
    }

    @Test
    void testCreateSession() {
        VotingSessionRequest request = new VotingSessionRequest();
        Long meetingAgendaId = 1L;
        ApplicationResponse expectedResponse = createDefault();
        when(service.createVotingSession(any(VotingSessionRequest.class), eq(meetingAgendaId)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApplicationResponse> response = meetingAgendaController.createSession(request, meetingAgendaId);

        assertEquals(expectedResponse, response.getBody());
        verify(service).createVotingSession(request, meetingAgendaId);
    }

    @Test
    void testClose() {
        Long meetingAgendaId = 1L;
        ApplicationResponse expectedResponse = createDefault();
        when(service.close(meetingAgendaId)).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApplicationResponse> response = meetingAgendaController.close(meetingAgendaId);

        assertEquals(expectedResponse, response.getBody());
        verify(service).close(meetingAgendaId);
    }

    @Test
    void testList() {
        List<MeetingAgendaVO> expectedList = Collections.singletonList(new MeetingAgendaVO(1L,"Tema"));
        when(service.findAll()).thenReturn(ResponseEntity.ok(expectedList));

        ResponseEntity<List<MeetingAgendaVO>> response = meetingAgendaController.list();

        assertEquals(expectedList, response.getBody());
        verify(service).findAll();
    }

    @Test
    void testListSessions() {
        Long meetingAgendaId = 1L;
        List<VotingSessionVO> expectedList = Collections.singletonList(new VotingSessionVO(1L,true, Instant.now().atZone(ZoneId.systemDefault())));
        when(service.findAllSessionsByMeetingAgenda(meetingAgendaId)).thenReturn(ResponseEntity.ok(expectedList));

        ResponseEntity<List<VotingSessionVO>> response = meetingAgendaController.listSessions(meetingAgendaId);

        assertEquals(expectedList, response.getBody());
        verify(service).findAllSessionsByMeetingAgenda(meetingAgendaId);
    }

    private ApplicationResponse createDefault() {
        return new ApplicationResponse("",1);
    }
}
