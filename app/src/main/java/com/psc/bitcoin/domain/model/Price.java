package com.psc.bitcoin.domain.model;

public class Price {
    private long unixTime;
    private float value;

    public Price(long unixTime, float value) {
        this.unixTime = unixTime;
        this.value = value;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "unixTime=" + unixTime +
                ", value=" + value +
                '}';
    }
}
