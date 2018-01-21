package com.psc.bitcoin.domain.usecase;

import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.domain.Repository;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.utils.CalendarUtils;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class FetchPricesUseCase implements UseCase<Calendar, Observable<List<Price>>> {
    private final Repository repository;
    private final CalendarUtils calendarUtils;
    private final SchedulerProvider schedulerProvider;

    public FetchPricesUseCase(final Repository repository, final CalendarUtils calendarUtils, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.calendarUtils = calendarUtils;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Observable<List<Price>> execute(Calendar currentDate) {
        Calendar date = repository.getLastFetchedDate();
        if (date == null) {
            return repository.fetchAllPrices()
                    .subscribeOn(schedulerProvider.getIoScheduler())
                    .compose(fetchedPrices -> storeDataAndGetPrices(fetchedPrices, currentDate));
        }

        int dayDifference = calendarUtils.calculateDifferenceInDays(currentDate, date);
        if (dayDifference > 0) {
            return repository.fetchPrices(dayDifference)
                    .subscribeOn(schedulerProvider.getIoScheduler())
                    .compose(fetchedPrices -> storeDataAndGetPrices(fetchedPrices, currentDate));
        }

        return repository.getAllPrices()
                .subscribeOn(schedulerProvider.getIoScheduler());
    }

    private ObservableSource<List<Price>> storeDataAndGetPrices(Observable<List<Price>> fetchedPrices, Calendar currentDate) {
        return fetchedPrices
                .doOnNext(repository::storePrices)
                .doOnNext(list -> repository.storeLastFetchedDate(currentDate))
                .flatMap(prices -> repository.getAllPrices());
    }

}
