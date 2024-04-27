package com.juliahaidarahmad.exchange.api.model;
import com.google.gson.annotations.SerializedName;

public class AdvancedTransaction {
    @SerializedName("amount")
    Float Amount;
    @SerializedName("less_than")
    boolean lessThan;

    public AdvancedTransaction(String usdAmount, String lbpAmount, String date, String direction) {
    }

    @SerializedName("usd_to_lbp")
    Boolean usdToLbp;
    @SerializedName("buy")
    Boolean Buy;
    @SerializedName("amount_to_buy")
    Float AmountToBuy;
    public AdvancedTransaction(Float Amount, Boolean lessThan, Boolean usdToLbp, Boolean Buy, Float AmountToBuy)
    {
        this.Amount=Amount;
        this.lessThan=lessThan;
        this.Buy=Buy;
        this.usdToLbp = usdToLbp;
        this.AmountToBuy=AmountToBuy;
    }
}