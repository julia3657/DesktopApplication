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
    }

    public List<Table> toStatTableObj(){
        List<Table> result = new ArrayList<>();

        Table maxStat = new Table("Mean", usd_to_lbp.max, lbp_to_usd.max);
        Table medianStat = new Table("Median", usd_to_lbp.median, lbp_to_usd.median);
        Table stddevStat = new Table("Maximum", usd_to_lbp.stdDev, lbp_to_usd.stdDev);
        Table varianceStat = new Table("Minimum", usd_to_lbp.variance, lbp_to_usd.variance);
        Table modeStat = new Table("Standard Deviation", usd_to_lbp.mode, lbp_to_usd.mode);

        result.add(maxStat);
        result.add(medianStat);
        result.add(stddevStat);
        result.add(varianceStat);
        result.add(modeStat);

        return result;
    }
}
