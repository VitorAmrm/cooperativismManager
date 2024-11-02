package com.amorim.cooperativism.manager.domain.to;

import jakarta.validation.constraints.NotEmpty;

public class MeetingAgendaRequest {
    @NotEmpty
    private String subject;

    public @NotEmpty String getSubject() {
        return subject;
    }

    public void setSubject(@NotEmpty String subject) {
        this.subject = subject;
    }
}
