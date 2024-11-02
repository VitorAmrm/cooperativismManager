package com.amorim.cooperativism.manager.repository;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingAgendaRepository extends JpaRepository<MeetingAgenda, Long> {
}
