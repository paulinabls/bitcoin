package com.psc.bitcoin.data.model;

import java.util.List;

public class MarketPriceResponse {
    private String status;

    private String unit;

    private List<Value> values;

    private String description;

    private String name;

    private String period;

    public MarketPriceResponse(String status, String unit, List<Value> values) {
        this.status = status;
        this.unit = unit;
        this.values = values;
    }

    public String getUnit() {
        return unit;
    }

    public List<Value> getValues() {
        return values;
    }

    public String getStatus() {
        return status;
    }


    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return period;
    }

    public boolean isStatusOK() {
        return "ok".equals(status);
    }
}
