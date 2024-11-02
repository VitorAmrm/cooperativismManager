package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.event.VotingSessionCloseEvent;
import com.amorim.cooperativism.manager.kafka.VotingSessionResultKafkaClient;
import com.amorim.cooperativism.manager.repository.VotingSessionRepository;
import com.amorim.cooperativism.manager.schedule.VotingSessionCloseScheduler;
import com.amorim.cooperativism.manager.service.VoteService;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class VotingSessionServiceImpl implements VotingSessionService {

    private final VotingSessionRepository repository;
    private final VotingSessionCloseScheduler scheduler;
    private final VoteService voteService;
    private final VotingSessionResultKafkaClient client;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger LOG = LoggerFactory.getLogger(VotingSessionServiceImpl.class);

    public VotingSessionServiceImpl(VotingSessionRepository repository, VotingSessionCloseScheduler scheduler, VoteService voteService, VotingSessionResultKafkaClient client, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.scheduler = scheduler;
        this.voteService = voteService;
        this.client = client;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ResponseEntity<ApplicationResponse> open(Long id) {
        Optional<VotingSession> optVotingSession = this.repository.findById(id);

        if (optVotingSession.isEmpty())
            return ResponseEntity.badRequest().body(new ApplicationResponse("Sessão não encontrada",HttpStatus.BAD_REQUEST.value()) );

        VotingSession votingSession = optVotingSession.get();

        if(Objects.nonNull(votingSession.getClosedAt()))
            return ResponseEntity.badRequest().body(new ApplicationResponse("Sessão já foi fechada",HttpStatus.BAD_REQUEST.value()) );

        if(Objects.nonNull(votingSession.getOpenedAt()))
            return ResponseEntity.badRequest().body(new ApplicationResponse("Sessão já está aberta", HttpStatus.BAD_REQUEST.value()));

        votingSession.setOpenedAt(new Date());
        repository.save(votingSession);

        scheduler.closeAt(createCloseTask(votingSession.getId()), Instant.now().plusMillis(votingSession.getTimeInMilliseconds()));

        return ResponseEntity.ok(new ApplicationResponse("Sessão aberta com sucesso. Essa sessão será fechada as {}", HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApplicationResponse> create(VotingSessionRequest request, MeetingAgenda agenda) {
        VotingSession session = new VotingSession();
        String message = "Sessão criada com sucesso.";

        session.setAgenda(agenda);

        if (Objects.nonNull(request.getTemporalQuantity()) && Objects.nonNull(request.getTemporalType())) {
            session.setTimeInMilliseconds(  Duration.of( request.getTemporalQuantity(), ChronoUnit.valueOf(request.getTemporalType() ) ).toMillis() );
        }

        if(request.getOpenWhenCreate()) {
            session.setOpenedAt(new Date());
            message += " Essa sessão será fechada as {}";
            scheduler.closeAt( createCloseTask(session.getId()), Instant.now().plusMillis( session.getTimeInMilliseconds() ) );
        }

        this.repository.save(session);
        return ResponseEntity.ok(new ApplicationResponse(message, HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApplicationResponse> vote(Long votingSessionId, VoteRequest request) {
        Optional<VotingSession> optSession = this.repository.findById(votingSessionId);

        if(optSession.isEmpty())
            return ResponseEntity.badRequest().body( new ApplicationResponse("Sessão de Votação não existe", HttpStatus.BAD_REQUEST.value()));

        VotingSession session = optSession.get();
        if(Objects.isNull(session.getOpenedAt()))
            return ResponseEntity.badRequest().body( new ApplicationResponse("Sessão de Votação ainda não foi aberta", HttpStatus.BAD_REQUEST.value()));

        return this.voteService.vote(request, session);
    }

    private Runnable createCloseTask(Long id) {
        return () -> this.eventPublisher.publishEvent(new VotingSessionCloseEvent(id));
    }
}
