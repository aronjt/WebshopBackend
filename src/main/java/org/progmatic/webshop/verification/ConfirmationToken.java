package org.progmatic.webshop.verification;

import org.progmatic.webshop.model.User;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long tokenid;

    @Column
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

    public ConfirmationToken() {

    }

    public void setTokenid(long tokenid) {
        this.tokenid = tokenid;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTokenid() {
        return tokenid;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public User getUser() {
        return user;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }
    // getters and setters
}
