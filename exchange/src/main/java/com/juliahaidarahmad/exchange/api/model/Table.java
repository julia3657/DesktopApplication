package com.juliahaidarahmad.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Table {
    @SerializedName("statName")
    String statName ="";

    @SerializedName("usd_to_lbp")
    String usdToLbp;

    @SerializedName("lbp_to_usd")
    String lbpToUsd;

    public Table(String name, String utol, String ltou) {
        this.statName = name;
        this.usdToLbp = utol;
        this.lbpToUsd = ltou;
    }

    public String getUsdToLbp() {
        return usdToLbp;
    }

    public String getLbpToUsd() {
        return lbpToUsd;
    }

    public String getStatName() {
        return statName;
    }
}
