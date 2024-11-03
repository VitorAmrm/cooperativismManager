package com.amorim.cooperativism.manager.domain;

import com.amorim.cooperativism.manager.domain.generic.PersistableEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "dmn_vote")
@SequenceGenerator(name = "generator", sequenceName = "sq_vote", allocationSize = 1)
@Inheritance(strategy = InheritanceType.JOINED)
@AttributeOverride( name = "id", column = @Column( name = "vote_id"))
public class Vote extends PersistableEntity {

    private Boolean value;
    private Date createdAt;
    private VotingSession session;
    private String memberNationalId;

    public Vote() {
    }

    public Vote(Boolean value, Date createdAt, VotingSession session, String memberNationalId) {
        this.value = value;
        this.createdAt = createdAt;
        this.session = session;
        this.memberNationalId = memberNationalId;
    }

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
    @Column(name = "member_national_id")
    public String getMemberNationalId() {
        return memberNationalId;
    }

    public void setMemberNationalId(String memberNationalId) {
        this.memberNationalId = memberNationalId;
    }
}
