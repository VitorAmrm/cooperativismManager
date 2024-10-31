package com.amorim.cooperativism.manager.domain;

import com.amorim.cooperativism.manager.domain.generic.PersistableEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "dmn_voting_session")
@SequenceGenerator(name = "generator", sequenceName = "sq_voting_session")
@Inheritance(strategy = InheritanceType.JOINED)
@AttributeOverride( name = "id", column = @Column( name = "voting_session_id"))
public class VotingSession  extends PersistableEntity {

    private Date openedAt;
    private Date closedAt;
    private List<Vote> votes;
    private MeetingAgenda agenda;

    public Date getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Date openedAt) {
        this.openedAt = openedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "vote_id")
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_agenda_id")
    public MeetingAgenda getAgenda() {
        return agenda;
    }

    public void setAgenda(MeetingAgenda agenda) {
        this.agenda = agenda;
    }
}
