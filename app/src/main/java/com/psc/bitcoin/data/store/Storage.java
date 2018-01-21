package com.psc.bitcoin.data.store;

import com.psc.bitcoin.domain.model.Price;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

public interface Storage {
    Calendar getLastFetchedDate();

    void savePrices(List<Price> prices);

    void saveLastFetchedDate(Calendar date);

    Observable<List<Price>> getAllPrices();
}
