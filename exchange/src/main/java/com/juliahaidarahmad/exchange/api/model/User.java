package com.juliahaidarahmad.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_name")
    String username;
    @SerializedName("password")
    String password;

    @SerializedName("balance_usd")
    Float balanceUsd;
    @SerializedName("balance_lbp")
    Float balanceLbp;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Float getUsdBalance() {
        return balanceUsd;
    }
    public Float getLbpBalance() {
        return balanceLbp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id=id;
    }
}
