package com.juliahaidarahmad.exchange.api.model;

import com.google.gson.annotations.SerializedName;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    public ConversionStats usd_to_lbp;
    public ConversionStats lbp_to_usd;


    public static class ConversionStats {
        @SerializedName("mean")
        public Float max;
        @SerializedName("median")
        public Float median;
        @SerializedName("max")
        public Float stdDev;
        @SerializedName("min")
        public Float mode;
        @SerializedName("std")
        public Float variance;
        @SerializedName("count")
        public Float count;
        @SerializedName("volatility")
        public Float volatility;
        @SerializedName("trend")
        public String trend;
    }

    public List<Table> toStatTableObj(){
        List<Table> result = new ArrayList<>();

        Table maxStat = new Table("Mean", usd_to_lbp.max.toString(), lbp_to_usd.max.toString());
        Table medianStat = new Table("Median", usd_to_lbp.median.toString(), lbp_to_usd.median.toString());
        Table stddevStat = new Table("Maximum", usd_to_lbp.stdDev.toString(), lbp_to_usd.stdDev.toString());
        Table varianceStat = new Table("Minimum", usd_to_lbp.variance.toString(), lbp_to_usd.variance.toString());
        Table modeStat = new Table("Standard Deviation", usd_to_lbp.mode.toString(), lbp_to_usd.mode.toString());
        Table countStat = new Table("Count", usd_to_lbp.count.toString(), lbp_to_usd.count.toString());
        Table volatilityStat = new Table("Volatility", usd_to_lbp.volatility.toString(), lbp_to_usd.volatility.toString());
        Table trendStat = new Table("Trend", usd_to_lbp.trend, lbp_to_usd.trend);

        result.add(maxStat);
        result.add(medianStat);
        result.add(stddevStat);
        result.add(varianceStat);
        result.add(modeStat);
        result.add(countStat);
        result.add(volatilityStat);
        result.add(trendStat);

        return result;
    }
}
