package com.amorim.cooperativism.manager.domain;

import com.amorim.cooperativism.manager.domain.generic.PersistableEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "dmn_meeting_agenda")
@SequenceGenerator(name = "generator", sequenceName = "sq_meeting_agenda")
@Inheritance(strategy = InheritanceType.JOINED)
@AttributeOverride( name = "id", column = @Column( name = "meeting_agenda_id"))
public class MeetingAgenda extends PersistableEntity {

    private String subject;
    private Date createdAt;
    private MeetingAgendaStatus status;
    private List<VotingSession> votingSession;

    @Column(name = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    @Column(name = "created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    @Enumerated(EnumType.STRING)
    public MeetingAgendaStatus getStatus() {
        return status;
    }

    public void setStatus(MeetingAgendaStatus status) {
        this.status = status;
    }
    @OneToMany
    @JoinColumn(name = "meeting_agenda_id")
    public List<VotingSession> getVotingSession() {
        return votingSession;
    }

    public void setVotingSession(List<VotingSession> votingSession) {
        this.votingSession = votingSession;
    }
}
