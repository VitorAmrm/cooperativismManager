package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionVO;
import com.amorim.cooperativism.manager.kafka.VotingSessionResultKafkaClient;
import com.amorim.cooperativism.manager.repository.VotingSessionRepository;
import com.amorim.cooperativism.manager.schedule.VotingSessionCloseScheduler;
import com.amorim.cooperativism.manager.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VotingSessionServiceImplTest {

    @Mock
    private VotingSessionRepository repository;

    @Mock
    private VotingSessionCloseScheduler scheduler;

    @Mock
    private VoteService voteService;

    @Mock
    private VotingSessionResultKafkaClient client;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private VotingSessionServiceImpl votingSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOpen_WhenSessionNotFound() {
        Long sessionId = 1L;

        when(repository.findById(sessionId)).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = votingSessionService.open(sessionId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sessão não encontrada", response.getBody().getMessage());
    }

    @Test
    void testOpen_WhenSessionAlreadyClosed() {
        Long sessionId = 1L;
        VotingSession session = new VotingSession();
        session.setClosedAt(new Date());

        when(repository.findById(sessionId)).thenReturn(Optional.of(session));

        ResponseEntity<ApplicationResponse> response = votingSessionService.open(sessionId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sessão já foi fechada", response.getBody().getMessage());
    }

    @Test
    void testOpen_WhenSessionAlreadyOpened() {
        Long sessionId = 1L;
        VotingSession session = new VotingSession();
        session.setOpenedAt(new Date());

        when(repository.findById(sessionId)).thenReturn(Optional.of(session));

        ResponseEntity<ApplicationResponse> response = votingSessionService.open(sessionId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sessão já está aberta", response.getBody().getMessage());
    }

    @Test
    void testOpen_Success() {
        Long sessionId = 1L;
        VotingSession session = new VotingSession();
        session.setId(sessionId);
        session.setTimeInMilliseconds(10000L);

        when(repository.findById(sessionId)).thenReturn(Optional.of(session));

        ResponseEntity<ApplicationResponse> response = votingSessionService.open(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sessão aberta com sucesso. Essa sessão será fechada as {}", response.getBody().getMessage());
        verify(scheduler, times(1)).closeAt(any(Runnable.class), any());
    }

    @Test
    void testCreate() {
        VotingSessionRequest request = new VotingSessionRequest();
        request.setTemporalQuantity(1L);
        request.setTemporalType("MINUTES");
        request.setOpenWhenCreate(true);
        MeetingAgenda agenda = new MeetingAgenda();

        ResponseEntity<ApplicationResponse> response = votingSessionService.create(request, agenda);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sessão criada com sucesso. Essa sessão será fechada as {}", response.getBody().getMessage());

        ArgumentCaptor<VotingSession> sessionCaptor = ArgumentCaptor.forClass(VotingSession.class);
        verify(repository, times(1)).save(sessionCaptor.capture());
        VotingSession session = sessionCaptor.getValue();
        assertEquals(agenda, session.getAgenda());
        assertTrue(session.getOpenedAt() != null);
    }

    @Test
    void testVote_WhenSessionNotFound() {
        Long votingSessionId = 1L;
        VoteRequest request = new VoteRequest();

        when(repository.findById(votingSessionId)).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = votingSessionService.vote(votingSessionId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sessão de Votação não existe", response.getBody().getMessage());
    }

    @Test
    void testVote_WhenSessionNotOpened() {
        Long votingSessionId = 1L;
        VoteRequest request = new VoteRequest();
        VotingSession session = new VotingSession();

        when(repository.findById(votingSessionId)).thenReturn(Optional.of(session));

        ResponseEntity<ApplicationResponse> response = votingSessionService.vote(votingSessionId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sessão de Votação ainda não foi aberta", response.getBody().getMessage());
    }

    @Test
    void testFindByMeetingAgenda() {
        MeetingAgenda agenda = new MeetingAgenda();
        VotingSession session = new VotingSession();
        when(repository.findByAgenda(agenda)).thenReturn(Collections.singletonList(session));

        ResponseEntity<List<VotingSessionVO>> response = votingSessionService.findByMeetingAgenda(agenda);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
