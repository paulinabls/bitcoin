package com.psc.bitcoin.domain.utils;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class CalendarUtilsTest {
    CalendarUtils tested = new CalendarUtils();
    private final Calendar date1 = Calendar.getInstance();
    private final Calendar date2 = Calendar.getInstance();


    @Test
    public void calculateDifferenceInDays_forSameDays_returnsZero() throws Exception {
        int dayOfYear = 6;
        date1.set(Calendar.DAY_OF_YEAR, dayOfYear);
        date2.set(Calendar.DAY_OF_YEAR, dayOfYear);

        int difference = tested.calculateDifferenceInDays(date1, date2);

        assertEquals(0,difference);
    }


    @Test
    public void calculateDifferenceInDays_forFirstMinuteOfYearAndLastMinuteOfPreviousYear_shouldReturnOne() throws Exception {
        date1.set(2018,1,1,0,1);
        date2.set(2017,12,31,23,59);

        int difference = tested.calculateDifferenceInDays(date1, date2);

        assertEquals(1,difference);
    }

    @Test
    public void calculateDifferenceInDays_forDayAndFollowingDay_shouldReturnMinusOne() throws Exception {
        int dayOfYear = 6;
        date1.set(Calendar.DAY_OF_YEAR, dayOfYear);
        date2.set(Calendar.DAY_OF_YEAR, dayOfYear+1);

        int difference = tested.calculateDifferenceInDays(date1, date2);

        assertEquals(-1,difference);
    }
}