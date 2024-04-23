package com.juliahaidarahmad.exchange.api.model;

public class GraphPoint {
    private String date;
    private double rate;

    // Constructor
    public GraphPoint(String date, double rate) {
        this.date = date;
        this.rate = rate;
    }

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
