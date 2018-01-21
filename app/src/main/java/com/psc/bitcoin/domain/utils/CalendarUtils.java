package com.psc.bitcoin.domain.utils;

import java.util.Calendar;

public class CalendarUtils {

    private static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    /**
     * @return difference in days between date1 and date2.
     * Can be negative if date2 > date1
     */
    public int calculateDifferenceInDays(Calendar date1, Calendar date2) {
        resetToMidnight(date1);
        resetToMidnight(date2);
        long differenceInMillis = date1.getTimeInMillis() - date2.getTimeInMillis();
        return (int) (differenceInMillis/MILLIS_IN_DAY);
    }

    private void resetToMidnight(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE,0);
        date.set(Calendar.SECOND,0);
        date.set(Calendar.MILLISECOND,0);
    }
}
