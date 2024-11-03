package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.MeetingAgendaStatus;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaRequest;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaVO;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionVO;
import com.amorim.cooperativism.manager.repository.MeetingAgendaRepository;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class MeetingAgendaServiceImplTest {

    @Mock
    private MeetingAgendaRepository repository;

    @Mock
    private VotingSessionService votingService;

    @InjectMocks
    private MeetingAgendaServiceImpl meetingAgendaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        MeetingAgendaRequest request = new MeetingAgendaRequest();
        request.setSubject("Nova Pauta");

        ResponseEntity<ApplicationResponse> response = meetingAgendaService.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pauta criada com sucesso", response.getBody().getMessage());

        ArgumentCaptor<MeetingAgenda> agendaCaptor = ArgumentCaptor.forClass(MeetingAgenda.class);
        verify(repository, times(1)).save(agendaCaptor.capture());
        MeetingAgenda agenda = agendaCaptor.getValue();

        assertEquals("Nova Pauta", agenda.getSubject());
        assertEquals(MeetingAgendaStatus.OPEN, agenda.getStatus());
        assertInstanceOf(Date.class, agenda.getCreatedAt());
    }

    @Test
    void testCreateVotingSession_WhenAgendaNotFound() {
        VotingSessionRequest request = new VotingSessionRequest();
        Long agendaId = 1L;

        when(repository.findById(agendaId)).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = meetingAgendaService.createVotingSession(request, agendaId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Nenhuma Pauta foi encontrada", response.getBody().getMessage());
    }

    @Test
    void testCreateVotingSession_WhenAgendaClosed() {
        VotingSessionRequest request = new VotingSessionRequest();
        Long agendaId = 1L;

        MeetingAgenda agenda = new MeetingAgenda();
        agenda.setId(agendaId);
        agenda.setStatus(MeetingAgendaStatus.CLOSED);
        when(repository.findById(agendaId)).thenReturn(Optional.of(agenda));

        ResponseEntity<ApplicationResponse> response = meetingAgendaService.createVotingSession(request, agendaId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Esta Pauta já foi fechada", response.getBody().getMessage());
    }

    @Test
    void testClose_WhenAgendaNotFound() {
        Long agendaId = 1L;

        when(repository.findById(agendaId)).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = meetingAgendaService.close(agendaId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Nenhuma Pauta foi encontrada", response.getBody().getMessage());
    }

    @Test
    void testClose() {
        Long agendaId = 1L;

        MeetingAgenda agenda = new MeetingAgenda();
        agenda.setId(agendaId);
        agenda.setStatus(MeetingAgendaStatus.OPEN);
        when(repository.findById(agendaId)).thenReturn(Optional.of(agenda));

        ResponseEntity<ApplicationResponse> response = meetingAgendaService.close(agendaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pauta fechada com sucesso", response.getBody().getMessage());
        assertEquals(MeetingAgendaStatus.CLOSED, agenda.getStatus());
        verify(repository, times(1)).save(agenda);
    }

    @Test
    void testFindAll() {
        ResponseEntity<List<MeetingAgendaVO>> response = meetingAgendaService.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindAllSessionsByMeetingAgenda_WhenAgendaNotFound() {
        Long agendaId = 1L;

        when(repository.findById(agendaId)).thenReturn(Optional.empty());

        ResponseEntity<List<VotingSessionVO>> response = meetingAgendaService.findAllSessionsByMeetingAgenda(agendaId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}