package com.amorim.cooperativism.manager.feign.retryer;

import feign.RetryableException;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;

public class CustomRetryer extends Retryer.Default {
    @Value("${feign.retries.qtt:3}")
    private final int maxAttempts;
    @Value("${feign.backoffperiod.value:1000}")
    private final long backoffPeriod;

    public CustomRetryer() {
        this(3, 1000);
    }

    public CustomRetryer(int maxAttempts, long backoffPeriod) {
        super(backoffPeriod, backoffPeriod * 2, maxAttempts);
        this.maxAttempts = maxAttempts;
        this.backoffPeriod = backoffPeriod;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        super.continueOrPropagate(e);
    }
}

