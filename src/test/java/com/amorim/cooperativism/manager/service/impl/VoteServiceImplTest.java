package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.domain.Vote;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.domain.to.VoteRequest;
import com.amorim.cooperativism.manager.repository.VoteRepository;
import com.amorim.cooperativism.manager.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VoteServiceImplTest {

    @Mock
    private VoteRepository repository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private VoteServiceImpl voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVote_WhenMemberNotAllowed() {
        VoteRequest request = new VoteRequest();
        request.setNationalId("123");
        VotingSession session = new VotingSession();

        when(memberService.canVote(request.getNationalId())).thenReturn(false);

        ResponseEntity<ApplicationResponse> response = voteService.vote(request, session);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Associado não habilitado para votação", response.getBody().getMessage());
    }

    @Test
    void testVote_WhenMemberAlreadyVoted() {
        VoteRequest request = new VoteRequest();
        request.setNationalId("123");
        request.setValue(true);
        VotingSession session = new VotingSession();

        when(memberService.canVote(request.getNationalId())).thenReturn(true);
        when(repository.findBySessionAndMemberNationalId(session, request.getNationalId())).thenReturn(Optional.of(new Vote()));

        ResponseEntity<ApplicationResponse> response = voteService.vote(request, session);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Associado já votou nessa sessão", response.getBody().getMessage());
    }

    @Test
    void testVote_Success() {
        VoteRequest request = new VoteRequest();
        request.setNationalId("123");
        request.setValue(true);
        VotingSession session = new VotingSession();

        when(memberService.canVote(request.getNationalId())).thenReturn(true);
        when(repository.findBySessionAndMemberNationalId(session, request.getNationalId())).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = voteService.vote(request, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Voto cadastrado com sucesso", response.getBody().getMessage());

        ArgumentCaptor<Vote> voteCaptor = ArgumentCaptor.forClass(Vote.class);
        verify(repository, times(1)).save(voteCaptor.capture());
        Vote savedVote = voteCaptor.getValue();
        assertEquals(request.getValue(), savedVote.getValue());
        assertEquals(request.getNationalId(), savedVote.getMemberNationalId());
        assertNotNull(savedVote.getCreatedAt());
    }
}
