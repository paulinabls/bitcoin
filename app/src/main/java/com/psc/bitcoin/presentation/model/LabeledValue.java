package com.psc.bitcoin.presentation.model;

public class LabeledValue {
    private String label;
    private float value;

    public LabeledValue(String label, float value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public float getValue() {
        return value;
    }

}
