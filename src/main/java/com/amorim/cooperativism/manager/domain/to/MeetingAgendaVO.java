package com.amorim.cooperativism.manager.domain.to;

import com.amorim.cooperativism.manager.domain.MeetingAgenda;

public record MeetingAgendaVO(Long id, String subject) {

    public static MeetingAgendaVO  from(MeetingAgenda agenda){
        return new MeetingAgendaVO(agenda.getId(), agenda.getSubject());
    }
}
