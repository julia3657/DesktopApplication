package com.juliahaidarahmad.exchange.login;

public class ForgotPasswordRequest {
    public String user_name;

    public ForgotPasswordRequest(String userName) {
        this.user_name = userName;
    }

    public String getUserName() {
        return user_name;
    }
}