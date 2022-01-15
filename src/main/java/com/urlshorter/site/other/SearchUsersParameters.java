package com.urlshorter.site.other;

public class SearchUsersParameters {
    public String emailFragment;
    public String role;
    public boolean blocked;

    public SearchUsersParameters(String emailFragment, String role, boolean blocked) {
        this.emailFragment = emailFragment;
        this.role = role;
        this.blocked = blocked;
    }
}
