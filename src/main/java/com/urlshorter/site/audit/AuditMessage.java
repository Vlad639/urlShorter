package com.urlshorter.site.audit;

import com.urlshorter.site.models.Link;
import com.urlshorter.site.models.User;

public class AuditMessage {

    private final String login;
    private final String action;
    private final String object;

    public AuditMessage(String login, ActionEnum actionEnum, Link link) {
        this.login = login;
        this.action = actionEnum.toString();
        this.object = "type: LINK, id: " + link.getId() + ", short: " + link.getUrlToken() + ", long: " + link.getLongLink();;
    }

    public AuditMessage(String login, ActionEnum actionEnum, Link link, String newLongLink) {
        this.login = login;
        this.action = actionEnum.toString();
        this.object = "type: LINK, id: " + link.getId() + ", short: " + link.getUrlToken() + ", oldLink: " + link.getLongLink() + ", newLink: " + newLongLink;
    }

    public AuditMessage(String login, ActionEnum actionEnum, User user) {
        this.login = login;
        this.action = actionEnum.toString();
        this.object = "type: USER, id: " + user.getId() + ", email: " + user.getEmail() + ", role: " + user.getRole();
    }

    public AuditMessage(String login, ActionEnum actionEnum, String message) {
        this.login = login;
        this.action = actionEnum.toString();
        this.object = message;
    }


    public String getLogin() {
        return login;
    }

    public String getAction() {
        return action;
    }

    public String getObject() {
        return object;
    }

}
