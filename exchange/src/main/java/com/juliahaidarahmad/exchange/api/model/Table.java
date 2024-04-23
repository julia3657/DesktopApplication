package com.juliahaidarahmad.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Table {
    @SerializedName("statName")
    String statName ="";

    @SerializedName("usd_to_lbp")
    Float usdToLbp;

    @SerializedName("lbp_to_usd")
    Float lbpToUsd;

    public Table(String name, Float utol, Float ltou) {
        this.statName = name;
        this.usdToLbp = utol;
        this.lbpToUsd = ltou;
    }

    public Float getUsdToLbp() {
        return usdToLbp;
    }

    public Float getLbpToUsd() {
        return lbpToUsd;
    }

    public String getStatName() {
        return statName;
    }
}
