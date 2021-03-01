package org.progmatic.webshop.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for confirmation token. This is used for confirming users' accounts.<br>
 *     Columns:
 *     <ul>
 *         <li>long tokenid</li>
 *         <li>String confirmationToken</li>
 *         <li>LocalDateTime createdDate</li>
 *         <li>LocalDateTime enableDate</li>
 *         <li>{@link User} user</li>
 *     </ul>
 */
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long tokenid;

    @Column
    private String confirmationToken;

    @Column
    private LocalDateTime createdDate;

    private LocalDateTime enableDate;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = LocalDateTime.now();
        enableDate = createdDate.plusDays(2);
        confirmationToken = UUID.randomUUID().toString();
    }

    public ConfirmationToken() {}

    public LocalDateTime getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(LocalDateTime enableDate) {
        this.enableDate = enableDate;
    }

    public long getTokenid() {
        return tokenid;
    }

    public void setTokenid(long tokenid) {
        this.tokenid = tokenid;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

}
