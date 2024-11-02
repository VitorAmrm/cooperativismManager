package com.amorim.cooperativism.manager.kafka;

import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.kafka.message.VotingResult;
import com.amorim.cooperativism.manager.service.impl.VotingSessionServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VotingSessionResultKafkaClient {

    @Value("${kafka.voting.result.topic:voting-result}")
    private String votingResultTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(VotingSessionServiceImpl.class);

    public VotingSessionResultKafkaClient(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(VotingSession session) {
        try {

            VotingResult result = new VotingResult(
                    session.getVotes().stream().filter( vote -> Boolean.TRUE.equals(vote.getValue())).count(),
                    session.getVotes().stream().filter( vote -> Boolean.FALSE.equals(vote.getValue())).count(),
                    (long) session.getVotes().size(),
                    String.valueOf(session.getId()));

            kafkaTemplate.send(this.votingResultTopic, mapper.writeValueAsString(result));
            LOG.info("A mensagem {} foi enviada para o topico {}", result, this.votingResultTopic);
        } catch (JsonProcessingException e) {
            LOG.error("Ocorreu um erro ao tentar parsear o JSON",e);
        }
    }
}
