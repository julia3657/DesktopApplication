package com.juliahaidarahmad.exchange.api.model;


import java.util.List;

public class GraphsResponse {
    private CurrencyExchangeData lbp_to_usd;
    private CurrencyExchangeData usd_to_lbp;

    public CurrencyExchangeData getLbpToUsd() {
        return lbp_to_usd;
    }

    public void setLbpToUsd(CurrencyExchangeData lbp_to_usd) {
        this.lbp_to_usd = lbp_to_usd;
    }

    public CurrencyExchangeData getUsdToLbp() {
        return usd_to_lbp;
    }

    public void setUsdToLbp(CurrencyExchangeData usd_to_lbp) {
        this.usd_to_lbp = usd_to_lbp;
    }
}

