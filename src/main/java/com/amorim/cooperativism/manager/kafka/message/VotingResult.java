package com.amorim.cooperativism.manager.kafka.message;

public record VotingResult(Long agreed, Long disagreed, Long totalVotes, String sessionId){}
