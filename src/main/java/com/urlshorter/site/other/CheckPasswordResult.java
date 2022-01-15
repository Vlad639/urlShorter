package com.urlshorter.site.other;

public class CheckPasswordResult {
    public boolean passwordIsOk;
    public String message;

    public CheckPasswordResult(boolean passwordIsOk, String message) {
        this.passwordIsOk = passwordIsOk;
        this.message = message;
    }
}
