package com.psc.bitcoin.presentation.model;

import com.db.chart.model.LineSet;
import com.psc.bitcoin.domain.model.Price;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public static LineSet toLineSet(List<LabeledValue> valueList) {
        final LineSet lineSet = new LineSet();
        for (LabeledValue value : valueList) {
            lineSet.addPoint(value.getLabel(), value.getValue());
        }
        return lineSet;
    }
}
