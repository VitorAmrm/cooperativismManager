package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.Vote;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import com.amorim.cooperativism.manager.repository.VoteRepository;
import com.amorim.cooperativism.manager.service.MemberService;
import com.amorim.cooperativism.manager.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository repository;
    private final MemberService memberService;

    public VoteServiceImpl(VoteRepository repository, MemberService memberService) {
        this.repository = repository;
        this.memberService = memberService;
    }


    @Override
    public ResponseEntity<ApplicationResponse> vote(VoteRequest request, VotingSession session) {

        if(!this.memberService.canVote(request.getNationalId()))
            return ResponseEntity.badRequest().body( new ApplicationResponse("Associado não habilitado para votação", HttpStatus.BAD_REQUEST.value()) );

        if(this.repository.findBySessionAndMemberNationalId(session, request.getNationalId()).isPresent())
            return ResponseEntity.badRequest().body( new ApplicationResponse("Associado já votou nessa sessão", HttpStatus.BAD_REQUEST.value()));

        Vote vote = new Vote();
        vote.setCreatedAt(new Date());
        vote.setSession(session);
        vote.setValue(request.getValue());
        vote.setMemberNationalId(request.getNationalId());
        this.repository.save(vote);

        return ResponseEntity.ok(new ApplicationResponse("Voto cadastrado com sucesso", HttpStatus.OK.value()));
    }
}
