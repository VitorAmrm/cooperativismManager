package com.amorim.cooperativism.manager.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class VotingSessionCloseScheduler {

    private final TaskScheduler executor = new SimpleAsyncTaskScheduler();
    private static final Logger LOG = LoggerFactory.getLogger(VotingSessionCloseScheduler.class);


    public void closeAt(Runnable what, Instant when) {
        LOG.info("Executando a tarefa  no momento {}", what, when.atZone(ZoneId.systemDefault()));
        getExecutor().schedule(what, when);
    }

    public TaskScheduler getExecutor() {
        return executor;
    }
}
