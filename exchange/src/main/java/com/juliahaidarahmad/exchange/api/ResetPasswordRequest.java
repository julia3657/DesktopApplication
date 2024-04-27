package com.juliahaidarahmad.exchange.api;

public class ResetPasswordRequest {
    private String token;
    private String password;

    // Constructor
    public ResetPasswordRequest(String OTP, String newPassword) {
        this.token = OTP;
        this.password = newPassword;
    }

    // Getters and Setters
    public String getOTP() {
        return token;
    }

    public void setOTP(String OTP) {
        this.token = OTP;
    }

    public String getNewPassword() {
        return password;
    }

    public void setNewPassword(String newPassword) {
        this.password = newPassword;
    }
}
