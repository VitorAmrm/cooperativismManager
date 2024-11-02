package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.repository.VotingSessionRepository;
import com.amorim.cooperativism.manager.schedule.VotingSessionCloseScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VotingSessionServiceImplTest {

    @InjectMocks
    private VotingSessionServiceImpl service;

    @Mock
    private VotingSessionRepository repository;

    @Mock
    private VotingSessionCloseScheduler scheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOpen_VotingSessionNotFound() {
        Long votingSessionId = 1L;
        when(repository.findById(votingSessionId)).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = service.open(votingSessionId);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Sessão não encontrada", response.getBody().getMessage());
        verify(repository, times(1)).findById(votingSessionId);
    }

    @Test
    public void testOpen_Success() {
        Long votingSessionId = 1L;
        VotingSession session = new VotingSession();
        session.setId(votingSessionId);
        session.setTimeInMilliseconds(60000L); // 1 minuto
        when(repository.findById(votingSessionId)).thenReturn(Optional.of(session));
        when(repository.save(session)).thenReturn(session);

        ResponseEntity<ApplicationResponse> response = service.open(votingSessionId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Sessão aberta com sucesso. Essa sessão será fechada as {}", response.getBody().getMessage());
        verify(repository, times(1)).findById(votingSessionId);
        verify(repository, times(1)).save(session);
        verify(scheduler, times(1)).closeAt(any(Runnable.class), any(Instant.class));
    }

    @Test
    public void testCreate_Success() {
        // Arrange
        VotingSessionRequest request = new VotingSessionRequest();
        request.setOpenWhenCreate(true);
        request.setTemporalQuantity(1L);
        request.setTemporalType("MINUTES");

        MeetingAgenda agenda = new MeetingAgenda();
        VotingSession session = new VotingSession();
        when(repository.save(any(VotingSession.class))).thenReturn(session);

        ResponseEntity<ApplicationResponse> response = service.create(request, agenda);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Sessão criada com sucesso. Esta sessão se encerrará {}", response.getBody().getMessage());
        verify(repository, times(1)).save(any(VotingSession.class));
        verify(scheduler, times(1)).closeAt(any(Runnable.class), any(Instant.class));
    }

    @Test
    public void testCreate_NoOpenWhenCreate() {
        VotingSessionRequest request = new VotingSessionRequest();
        request.setOpenWhenCreate(false);
        MeetingAgenda agenda = new MeetingAgenda();
        VotingSession session = new VotingSession();
        when(repository.save(any(VotingSession.class))).thenReturn(session);

        ResponseEntity<ApplicationResponse> response = service.create(request, agenda);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Sessão criada com sucesso.", response.getBody().getMessage());
        verify(repository, times(1)).save(any(VotingSession.class));
        verify(scheduler, never()).closeAt(any(Runnable.class), any(Instant.class));
    }
}
