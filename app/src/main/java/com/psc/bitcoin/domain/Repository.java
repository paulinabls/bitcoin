package com.psc.bitcoin.domain;

import com.psc.bitcoin.domain.model.Price;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

public interface Repository {
    Observable<List<Price>> fetchPrices(int timespanInDays);

    Observable<List<Price>> fetchAllPrices();

    Calendar getLastFetchedDate();

    void storePrices(List<Price> prices);

    void storeLastFetchedDate(Calendar date);

    Observable<List<Price>> getAllPrices();

}
