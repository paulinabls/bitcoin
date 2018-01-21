package com.psc.bitcoin.data.store;

import com.psc.bitcoin.data.store.db.EntityMapper;
import com.psc.bitcoin.data.store.db.PriceDao;
import com.psc.bitcoin.data.store.db.PriceEntity;
import com.psc.bitcoin.domain.model.Price;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

public class LocalStorage implements Storage {
    private final EntityMapper entityMapper;
    private PriceDao priceDao;
    private ValueStorage valueStorage;

    public LocalStorage(PriceDao priceDao, ValueStorage valueStorage, EntityMapper entityMapper) {
        this.priceDao = priceDao;
        this.valueStorage = valueStorage;
        this.entityMapper = entityMapper;
    }

    @Override
    public Calendar getLastFetchedDate() {
        return valueStorage.getLastFetchedDate();
    }

    @Override
    public void savePrices(List<Price> prices) {
        EntityMapper mapper = entityMapper;
        PriceEntity[] priceArray = mapper.toEntities(prices);
        priceDao.insertAll(priceArray);
    }

    @Override
    public void saveLastFetchedDate(Calendar date) {
        valueStorage.saveLastFetchedDate(date);

    }

    @Override
    public Observable<List<Price>> getAllPrices() {
        return priceDao.getPrices()
                .defaultIfEmpty(Collections.emptyList())
                .map(entityMapper::fromEntities)
                .toObservable();
    }
}
