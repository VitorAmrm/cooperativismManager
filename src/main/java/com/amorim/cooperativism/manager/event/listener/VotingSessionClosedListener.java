package com.amorim.cooperativism.manager.event.listener;

import com.amorim.cooperativism.manager.event.VotingSessionCloseEvent;
import com.amorim.cooperativism.manager.kafka.VotingSessionResultKafkaClient;
import com.amorim.cooperativism.manager.repository.VotingSessionRepository;
import com.amorim.cooperativism.manager.service.impl.VotingSessionServiceImpl;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class VotingSessionClosedListener {

    private final VotingSessionRepository repository;
    private final VotingSessionResultKafkaClient client;
    private static final Logger LOG = LoggerFactory.getLogger(VotingSessionClosedListener.class);


    public VotingSessionClosedListener(VotingSessionRepository repository, VotingSessionResultKafkaClient client) {
        this.repository = repository;
        this.client = client;
    }

    @EventListener
    @Transactional
    public void handleVotingSessionClosedEvent(VotingSessionCloseEvent event) {
        LOG.info("Um evento para fechar a Sessão {} chegou", event.sessionId());
        this.repository.findById(event.sessionId()).ifPresent(
                (session) -> {
                    session.setClosedAt( new Date() );
                    client.sendMessage(session);
                }
        );
        System.out.println("A sessão de votação de ID " + event.sessionId() + " foi fechada.");
    }
}
