package com.psc.bitcoin.data.model;

public class Value {
    private long x;
    private float y;

    public Value(long x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public long getX() {
        return x;
    }

    @Override
    public String toString() {
        return "ClassPojo [y = " + y + ", x = " + x + "]";
    }
}
