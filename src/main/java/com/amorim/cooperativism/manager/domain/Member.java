package com.amorim.cooperativism.manager.domain;

import com.amorim.cooperativism.manager.domain.generic.PersistableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "dmn_member")
@SequenceGenerator(name = "generator", sequenceName = "sq_member")
@Inheritance(strategy = InheritanceType.JOINED)
@AttributeOverride( name = "id", column = @Column( name = "member_id"))
public class Member extends PersistableEntity {

    private String nationalId;
    /*
    Posso estar associado a mais de uma sessão ao mesmo tempo?
     */
    private VotingSession session;

    @Column(name = "national_id")
    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voting_session_id")
    public VotingSession getSession() {
        return session;
    }

    public void setSession(VotingSession session) {
        this.session = session;
    }
}
