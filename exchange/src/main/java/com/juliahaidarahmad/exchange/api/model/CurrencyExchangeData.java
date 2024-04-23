package com.juliahaidarahmad.exchange.api.model;

import java.util.List;

public class CurrencyExchangeData {
    public List<String> dates;
    public List<Double> values;

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }
}
