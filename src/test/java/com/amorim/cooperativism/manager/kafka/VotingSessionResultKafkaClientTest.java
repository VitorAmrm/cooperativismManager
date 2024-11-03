package com.amorim.cooperativism.manager.kafka;

import com.amorim.cooperativism.manager.domain.Vote;
import com.amorim.cooperativism.manager.domain.VotingSession;
import com.amorim.cooperativism.manager.kafka.message.VotingResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class VotingSessionResultKafkaClientTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private VotingSessionResultKafkaClient votingSessionResultKafkaClient;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(votingSessionResultKafkaClient, "votingResultTopic", "voting-result");
    }

    @Test
    void testSendMessage() throws JsonProcessingException {
        VotingSession session = createVotingSession();
        String expectedTopic = "voting-result";
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        votingSessionResultKafkaClient.sendMessage(session);

        verify(kafkaTemplate, times(1)).send(eq(expectedTopic), messageCaptor.capture());
        String actualMessage = messageCaptor.getValue();
        VotingResult actualResult = objectMapper.readValue(actualMessage, VotingResult.class);

        assertEquals(3, actualResult.totalVotes());
        assertEquals(2, actualResult.agreed());
        assertEquals(1, actualResult.disagreed());
        assertEquals(String.valueOf(session.getId()), actualResult.sessionId());
    }

    private VotingSession createVotingSession() {
        VotingSession session = new VotingSession();
        session.setId(1L);
        session.setVotes(List.of(
                new Vote(true, new Date(),session,"12312312231"),
                new Vote(true, new Date(),session,"12312312231"),
                new Vote(false, new Date(),session,"12312312231")
        ));
        return session;
    }
}
