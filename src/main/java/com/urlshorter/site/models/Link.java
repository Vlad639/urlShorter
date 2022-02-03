package com.urlshorter.site.models;

import javax.persistence.*;

@Entity
@Table(name = "links")
public class Link {

    @Id
    @Column(name = "link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "url_token")
    private String urlToken;

    @Column(name ="long_link")
    private String longLink;

    @Column(name = "clicks")
    private int clicks;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    public Link(){}



    public Link(User user, String urlToken, String longLink){
        this.user = user;
        this.urlToken = urlToken;
        this.longLink = longLink;
    }

    public Link(long id, String urlToken, String longLink, int clicks) {
        this.id = id;
        this.urlToken = urlToken;
        this.longLink = longLink;
        this.clicks = clicks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrlToken() {
        return urlToken;
    }

    public void setUrlToken(String urlToken) {
        this.urlToken = urlToken;
    }

    public String getLongLink() {
        return longLink;
    }

    public void setLongLink(String longLink) {
        this.longLink = longLink;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
