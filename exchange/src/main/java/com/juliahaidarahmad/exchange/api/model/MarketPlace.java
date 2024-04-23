package com.juliahaidarahmad.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class MarketPlace {
    @SerializedName("id")
    private Integer id;

    @SerializedName("usd_amount")
    private Float usdAmount;

    @SerializedName("lbp_amount")
    private Float lbpAmount;

    @SerializedName("usd_to_lbp")
    private Boolean usdToLbp;


    @SerializedName("added_date")
    private String addedDate;

    public MarketPlace(Integer id, Float usdAmount, Float lbpAmount, Boolean usdToLbp) {
        this.id = id;
        this.usdAmount = usdAmount;
        this.lbpAmount = lbpAmount;
        this.usdToLbp = usdToLbp;
    }


    public Integer getId() {
        return id;
    }

    public Float getUsdAmount() {
        return usdAmount;
    }

    public Float getLbpAmount() {
        return lbpAmount;
    }

    public Boolean getUsdToLbp() {
        return usdToLbp;
    }

    public String getAddedDate() {
        return addedDate;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsdAmount(Float usdAmount) {
        this.usdAmount = usdAmount;
    }

    public void setLbpAmount(Float lbpAmount) {
        this.lbpAmount = lbpAmount;
    }

    public void setUsdToLbp(Boolean usdToLbp) {
        this.usdToLbp = usdToLbp;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}
