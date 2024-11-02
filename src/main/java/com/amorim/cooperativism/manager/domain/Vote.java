package com.amorim.cooperativism.manager.domain;

import com.amorim.cooperativism.manager.domain.generic.PersistableEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "dmn_vote")
@SequenceGenerator(name = "generator", sequenceName = "sq_vote")
@Inheritance(strategy = InheritanceType.JOINED)
@AttributeOverride( name = "id", column = @Column( name = "vote_id"))
public class Vote extends PersistableEntity {

    private Boolean value;
    private Date createdAt;
    private VotingSession session;

    @Column(name = "value")
    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
    @Column(name = "created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_session_id")
    public VotingSession getSession() {
        return session;
    }

    public void setSession(VotingSession session) {
        this.session = session;
    }
}
