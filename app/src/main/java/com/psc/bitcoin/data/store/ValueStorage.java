package com.psc.bitcoin.data.store;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Calendar;

public class ValueStorage {
    private static final String LAST_FETCHED_DATE_MILLIS = "LAST_FETCHED_DATE_MILLIS";
    private SharedPreferences preferences;

    public ValueStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Nullable
    public Calendar getLastFetchedDate() {
        long dateMillis = preferences.getLong(LAST_FETCHED_DATE_MILLIS, -1);
        if (dateMillis == -1) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMillis);
        return calendar;
    }

    public void saveLastFetchedDate(Calendar date) {
        preferences.edit()
                .putLong(LAST_FETCHED_DATE_MILLIS, date.getTimeInMillis())
                .apply();
    }
}
