package com.psc.bitcoin.domain.usecase;

import com.psc.bitcoin.domain.Repository;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class FetchPricesUseCase implements UseCase<Calendar, Observable<List<Price>>> {
    private final Repository repository;
    private final CalendarUtils calendarUtils;

    public FetchPricesUseCase(final Repository repository, final CalendarUtils calendarUtils) {
        this.repository = repository;
        this.calendarUtils = calendarUtils;
    }

    @Override
    public Observable<List<Price>> execute(Calendar currentDate) {
        return Observable.fromCallable(fetchDataIfNeeded(currentDate))
                .map(list -> storeDataIfNeededAndGetPrices(list, currentDate));
    }

    private List<Price> storeDataIfNeededAndGetPrices(List<Price> list, Calendar currentDate) {
        if (!list.isEmpty()) {
            repository.storePrices(list);
            repository.storeLastFetchedDate(currentDate);
        }
        return repository.getAllPrices()
                .defaultIfEmpty(Collections.emptyList())
                .blockingFirst();
    }

    /**
     * if data was never fetched - fetch all prices till now
     * else calculate how many days ago was it last fetched - fetched only missing period of data
     *
     * @param currentDate - will be compared with last fetched date
     * @return fetchedList of prices
     */
    private Callable<List<Price>> fetchDataIfNeeded(Calendar currentDate) {
        return () -> {
            List<Price> fetchedList = Collections.emptyList();

            Calendar date = repository.getLastFetchedDate();
            if (date == null) {
                fetchedList = repository.fetchAllPrices().blockingFirst();
            } else {
                int dayDifference = calendarUtils.calculateDifferenceInDays(currentDate, date);
                if (dayDifference > 0) {
                    fetchedList = repository.fetchPrices(dayDifference).blockingFirst();
                }
            }

            return fetchedList;
        };
    }

}
