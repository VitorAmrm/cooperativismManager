package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.MeetingAgendaStatus;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.repository.MeetingAgendaRepository;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MeetingAgendaServiceImplTest {

    @InjectMocks
    private MeetingAgendaServiceImpl service;

    @Mock
    private MeetingAgendaRepository repository;

    @Mock
    private VotingSessionService votingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        // Arrange
        MeetingAgendaRequest request = new MeetingAgendaRequest();
        MeetingAgenda agenda = new MeetingAgenda();
        agenda.setSubject("Teste de Pauta");

        // Simula o salvamento
        when(repository.save(any(MeetingAgenda.class))).thenReturn(agenda);

        // Act
        ResponseEntity<ApplicationResponse> response = service.create(request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Pauta creada com sucesso", response.getBody().getMessage());
        verify(repository, times(1)).save(any(MeetingAgenda.class));
    }

    @Test
    public void testCreateVotingSession_MeetingAgendaNotFound() {
        // Arrange
        VotingSessionRequest request = new VotingSessionRequest();
        Long meetingAgendaId = 1L;

        when(repository.findById(meetingAgendaId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApplicationResponse> response = service.createVotingSession(request, meetingAgendaId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Nenhuma Pauta foi encontrada", response.getBody().getMessage());
        verify(repository, times(1)).findById(meetingAgendaId);
    }

    @Test
    public void testCreateVotingSession_MeetingAgendaClosed() {
        // Arrange
        VotingSessionRequest request = new VotingSessionRequest();
        Long meetingAgendaId = 1L;

        MeetingAgenda agenda = new MeetingAgenda();
        agenda.setStatus(MeetingAgendaStatus.CLOSED);
        when(repository.findById(meetingAgendaId)).thenReturn(Optional.of(agenda));

        // Act
        ResponseEntity<ApplicationResponse> response = service.createVotingSession(request, meetingAgendaId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Esta Pauta já foi fechada", response.getBody().getMessage());
        verify(repository, times(1)).findById(meetingAgendaId);
    }

    @Test
    public void testCreateVotingSession_Success() {
        // Arrange
        VotingSessionRequest request = new VotingSessionRequest();
        Long meetingAgendaId = 1L;

        MeetingAgenda agenda = new MeetingAgenda();
        agenda.setStatus(MeetingAgendaStatus.OPEN);
        when(repository.findById(meetingAgendaId)).thenReturn(Optional.of(agenda));
        when(votingService.create(request, agenda)).thenReturn(ResponseEntity.ok(new ApplicationResponse("Sessão de votação criada", HttpStatus.OK.value())));

        // Act
        ResponseEntity<ApplicationResponse> response = service.createVotingSession(request, meetingAgendaId);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Sessão de votação criada", response.getBody().getMessage());
        verify(repository, times(1)).findById(meetingAgendaId);
        verify(votingService, times(1)).create(request, agenda);
    }

    @Test
    public void testClose_MeetingAgendaNotFound() {
        // Arrange
        Long meetingAgendaId = 1L;

        when(repository.findById(meetingAgendaId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApplicationResponse> response = service.close(meetingAgendaId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Nenhuma Pauta foi encontrada", response.getBody().getMessage());
        verify(repository, times(1)).findById(meetingAgendaId);
    }

    @Test
    public void testClose_Success() {
        // Arrange
        Long meetingAgendaId = 1L;
        MeetingAgenda agenda = new MeetingAgenda();
        agenda.setStatus(MeetingAgendaStatus.OPEN);
        when(repository.findById(meetingAgendaId)).thenReturn(Optional.of(agenda));

        // Act
        ResponseEntity<ApplicationResponse> response = service.close(meetingAgendaId);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Pauta fechada com sucesso", response.getBody().getMessage());
        assertEquals(MeetingAgendaStatus.CLOSED, agenda.getStatus());
        verify(repository, times(1)).findById(meetingAgendaId);
        verify(repository, times(1)).save(agenda);
    }
}
