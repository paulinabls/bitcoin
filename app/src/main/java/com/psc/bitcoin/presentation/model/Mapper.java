package com.psc.bitcoin.presentation.model;

import com.psc.bitcoin.domain.model.Price;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mapper {
    public static final String MONTH_YEAR = "MMM''yy";
    public static final String DAY_MONTH = "d.MMM";
    private final SimpleDateFormat dateFormatter;

    public Mapper(SimpleDateFormat formatter) {
        dateFormatter = formatter;
    }


    public LabeledValue withDateLabel(Price p) {
        Date date = new Date(p.getUnixTime()* 1000);
        return new LabeledValue(dateFormatter.format(date), p.getValue());
    }

    public LabeledValue withEmptyLabel(Price p) {
        return new LabeledValue("", p.getValue());
    }
}
