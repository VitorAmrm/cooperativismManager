package com.amorim.cooperativism.manager.domain.to;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class VotingSessionRequest {
    @Nullable
    private Long meetingAgendaId;
    @NotNull
    private Boolean openWhenCreate = false;
    private Long temporalQuantity;
    private String temporalType;

    public Long getMeetingAgendaId() {
        return meetingAgendaId;
    }

    public void setMeetingAgendaId(Long meetingAgendaId) {
        this.meetingAgendaId = meetingAgendaId;
    }

    public @NotNull Boolean getOpenWhenCreate() {
        return openWhenCreate;
    }

    public void setOpenWhenCreate(@NotNull Boolean openWhenCreate) {
        this.openWhenCreate = openWhenCreate;
    }

    public Long getTemporalQuantity() {
        return temporalQuantity;
    }

    public void setTemporalQuantity(Long temporalQuantity) {
        this.temporalQuantity = temporalQuantity;
    }

    public String getTemporalType() {
        return temporalType;
    }

    public void setTemporalType(String temporalType) {
        this.temporalType = temporalType;
    }
}
