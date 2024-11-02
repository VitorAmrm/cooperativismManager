package com.amorim.cooperativism.manager.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;

import static org.mockito.Mockito.*;

public class VotingSessionCloseSchedulerTest {

    private VotingSessionCloseScheduler scheduler;
    private TaskScheduler taskScheduler;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        taskScheduler = mock(TaskScheduler.class);
        scheduler = new VotingSessionCloseScheduler() {
            @Override
            public TaskScheduler getExecutor() {
                return taskScheduler;
            }
        };
    }

    @Test
    public void testCloseAt() {
        Runnable task = () -> System.out.println("Task executed");
        Instant when = Instant.now().plusSeconds(10);

        scheduler.closeAt(task, when);

        verify(taskScheduler, times(1)).schedule(task, when);
    }
}
