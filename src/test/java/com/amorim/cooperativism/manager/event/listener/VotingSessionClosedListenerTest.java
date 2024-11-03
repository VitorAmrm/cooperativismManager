package com.amorim.cooperativism.manager.event.listener;

import com.amorim.cooperativism.manager.event.VotingSessionCloseEvent;
import com.amorim.cooperativism.manager.kafka.VotingSessionResultKafkaClient;
import com.amorim.cooperativism.manager.repository.VotingSessionRepository;
import com.amorim.cooperativism.manager.domain.VotingSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

class VotingSessionClosedListenerTest {

    @InjectMocks
    private VotingSessionClosedListener votingSessionClosedListener;

    @Mock
    private VotingSessionRepository repository;

    @Mock
    private VotingSessionResultKafkaClient client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleVotingSessionClosedEvent() {
        Long sessionId = 1L;
        VotingSessionCloseEvent event = new VotingSessionCloseEvent(sessionId);
        VotingSession session = new VotingSession();
        session.setId(sessionId);

        when(repository.findById(eq(sessionId))).thenReturn(Optional.of(session));

        votingSessionClosedListener.handleVotingSessionClosedEvent(event);

        verify(repository).findById(sessionId);
        assertEquals(new Date().getDate(), session.getClosedAt().getDate());
        verify(client).sendMessage(session);
    }

    @Test
    void testHandleVotingSessionClosedEvent_NoSessionFound() {
        Long sessionId = 1L;
        VotingSessionCloseEvent event = new VotingSessionCloseEvent(sessionId);

        when(repository.findById(eq(sessionId))).thenReturn(Optional.empty());

        votingSessionClosedListener.handleVotingSessionClosedEvent(event);

        verify(repository).findById(sessionId);
        verify(client, never()).sendMessage(any());
    }
}
