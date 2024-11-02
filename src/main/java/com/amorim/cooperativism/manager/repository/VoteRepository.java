package com.amorim.cooperativism.manager.repository;

import com.amorim.cooperativism.manager.domain.Vote;
import com.amorim.cooperativism.manager.domain.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findBySessionAndMemberNationalId(VotingSession session, String  memberNationalId);
}
