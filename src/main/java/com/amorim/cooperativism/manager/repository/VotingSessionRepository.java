package com.amorim.cooperativism.manager.repository;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import com.amorim.cooperativism.manager.domain.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
    List<VotingSession> findByAgenda(MeetingAgenda agenda);
}
