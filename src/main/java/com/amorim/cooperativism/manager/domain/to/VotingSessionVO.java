package com.amorim.cooperativism.manager.domain.to;

import com.amorim.cooperativism.manager.domain.VotingSession;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public record VotingSessionVO(Long id, Boolean isOpen, ZonedDateTime shouldCloseAt){

    public static VotingSessionVO from(VotingSession session) {
        Boolean isOpen = Objects.nonNull(session.getOpenedAt()) && Objects.isNull(session.getClosedAt());
        return new VotingSessionVO(session.getId(), isOpen, isOpen ? session.getOpenedAt().toInstant().plusMillis(session.getTimeInMilliseconds()).atZone(ZoneId.systemDefault()): null);
    }
}
