package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.repository.VotingSessionRepository;
import com.amorim.cooperativism.manager.schedule.VotingSessionCloseScheduler;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(VotingSessionServiceImpl.class);

    public VotingSessionServiceImpl(VotingSessionRepository repository, VotingSessionCloseScheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;
    }

    @Override
    public ResponseEntity<ApplicationResponse> open(Long id) {
        Optional<VotingSession> votingSession = this.repository.findById(id);

        if (votingSession.isEmpty())
            return ResponseEntity.badRequest().body(new ApplicationResponse("Sessão não encontrada",HttpStatus.BAD_REQUEST.value()) );

        String message = "Sessão aberta com sucesso.";
        votingSession.ifPresent(
                (session) -> {
                    session.setOpenedAt(new Date());
                    repository.save(session);
                    scheduler.closeAt(createCloseTask(session.getId()), Instant.now().plusMillis(session.getTimeInMilliseconds()));
                }
        );

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

    private Runnable createCloseTask(Long id) {
        return () -> {
            this.repository.findById(id).ifPresent(
                    (session) -> {
                        session.setClosedAt(new Date());
                        this.repository.save(session);
                        LOG.info("A Sessão de Votação de Id {} foi fechada", id);
                    }
            );
        };
    }
}
