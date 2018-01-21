package com.psc.bitcoin.data;

import android.support.annotation.NonNull;

import com.psc.bitcoin.data.mapper.PriceMapper;
import com.psc.bitcoin.data.network.BlockchainApi;
import com.psc.bitcoin.data.store.Storage;
import com.psc.bitcoin.domain.Repository;
import com.psc.bitcoin.domain.model.Price;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

public class BitcoinRepository implements Repository {
    private static final String DAYS = "days";
    private static final String ALL = "all";
    private static final String JSON = "json";
    private final Storage store;
    private final PriceMapper priceMapper;
    private final SchedulerProvider schedulerProvider;
    private BlockchainApi api;

    public BitcoinRepository(final BlockchainApi api, final Storage storage, final PriceMapper priceMapper, final SchedulerProvider
            schedulerProvider) {
        this.api = api;
        store = storage;
        this.priceMapper = priceMapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Observable<List<Price>> fetchPrices(final int timespanInDays) {
        return api.getPrices(toTimeSpanParam(timespanInDays), JSON)
                .map(priceMapper::map)
                .subscribeOn(schedulerProvider.getIoScheduler())
                .observeOn(schedulerProvider.getMainScheduler());
    }

    @NonNull
    private String toTimeSpanParam(int timeSpanInDays) {
        return timeSpanInDays + DAYS;
    }

    @Override
    public Observable<List<Price>> fetchAllPrices() {
        return api.getPrices(ALL, JSON)
                .map(priceMapper::map)
                .subscribeOn(schedulerProvider.getIoScheduler())
                .observeOn(schedulerProvider.getMainScheduler());
    }

    @Override
    public Calendar getLastFetchedDate() {
        return store.getLastFetchedDate();
    }

    @Override
    public void storePrices(List<Price> prices) {
        store.savePrices(prices);
    }

    @Override
    public void storeLastFetchedDate(Calendar date) {
        store.saveLastFetchedDate(date);
    }

    @Override
    public Observable<List<Price>> getAllPrices() {
        return store.getAllPrices();
    }

}
