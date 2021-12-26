package com.urlshorter.site.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_blocked")
    private boolean blocked;

    @Column(name = "user_role")
    private int role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Link> links;

    public User(){};

    public User(long id, String email, boolean isBlocked, int role, List<Link> links) {
        this.id = id;
        this.email = email;
        this.blocked = isBlocked;
        this.role = role;
        this.links = links;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String userEmail) {
        this.email = userEmail;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        blocked = blocked;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
