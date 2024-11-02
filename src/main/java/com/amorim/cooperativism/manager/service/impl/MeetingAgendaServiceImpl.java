package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.MeetingAgendaStatus;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.MeetingAgendaRequest;
import com.amorim.cooperativism.manager.domain.to.VotingSessionRequest;
import com.amorim.cooperativism.manager.repository.MeetingAgendaRepository;
import com.amorim.cooperativism.manager.service.MeetingAgendaService;
import com.amorim.cooperativism.manager.service.VotingSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MeetingAgendaServiceImpl implements MeetingAgendaService {

    private final MeetingAgendaRepository repository;
    private final VotingSessionService votingService;

    public MeetingAgendaServiceImpl(MeetingAgendaRepository repository, VotingSessionService votingService) {
        this.repository = repository;
        this.votingService = votingService;
    }

    @Override
    public ResponseEntity<ApplicationResponse> create(MeetingAgendaRequest agendaRequest) {
        MeetingAgenda agenda = new MeetingAgenda();
        agenda.setSubject(agendaRequest.getSubject());
        agenda.setStatus(MeetingAgendaStatus.OPEN);
        agenda.setCreatedAt(new Date());
        this.repository.save(agenda);
        return ResponseEntity.ok( new ApplicationResponse("Pauta criada com sucesso", HttpStatus.OK.value()) );
    }

    @Override
    public ResponseEntity<ApplicationResponse> createVotingSession(VotingSessionRequest request, Long meetingAgendaId) {

        Optional<MeetingAgenda> optMeetingAgenda = this.repository.findById(meetingAgendaId);

        if(optMeetingAgenda.isEmpty())
            return ResponseEntity.badRequest().body(new ApplicationResponse("Nenhuma Pauta foi encontrada", HttpStatus.BAD_REQUEST.value()));

        MeetingAgenda agenda = optMeetingAgenda.get();

        if(MeetingAgendaStatus.CLOSED.equals(agenda.getStatus()))
            return ResponseEntity.badRequest().body(new ApplicationResponse("Esta Pauta já foi fechada", HttpStatus.BAD_REQUEST.value()));

        return this.votingService.create(request, agenda);
    }

    @Override
    public ResponseEntity<ApplicationResponse> close(Long meetingAgendaId) {
        Optional<MeetingAgenda> optMeetingAgenda = this.repository.findById(meetingAgendaId);

        if(optMeetingAgenda.isEmpty())
            return ResponseEntity.badRequest().body(new ApplicationResponse("Nenhuma Pauta foi encontrada", HttpStatus.BAD_REQUEST.value()));

        optMeetingAgenda.ifPresent(
                (agenda) -> {
                    agenda.setStatus(MeetingAgendaStatus.CLOSED);
                    this.repository.save(agenda);
                }
        );

        return ResponseEntity.ok( new ApplicationResponse("Pauta fechada com sucesso", HttpStatus.OK.value()));
    }
}
