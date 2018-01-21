package com.psc.bitcoin.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.db.chart.model.LineSet;
import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.usecase.FetchPricesUseCase;
import com.psc.bitcoin.domain.usecase.FilterChartDataUseCase;
import com.psc.bitcoin.presentation.model.Mapper;
import com.psc.bitcoin.presentation.model.LabeledValue;
import com.psc.bitcoin.presentation.presenter.base.Presenter;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class PricePresenter implements Presenter<PriceView> {

    private PriceView view;
    private final FetchPricesUseCase fetchPricesUseCase;
    private final SchedulerProvider schedulerProvider;
    private Disposable fetchSubscription;
    private Disposable filterSubscription;
    private List<Price> list = Collections.emptyList();
    private FilterChartDataUseCase filterDataUseCase;

    public PricePresenter(FetchPricesUseCase fetchPricesUseCase, FilterChartDataUseCase filterDataUseCase, SchedulerProvider schedulerProvider) {
        this.fetchPricesUseCase = fetchPricesUseCase;
        this.filterDataUseCase = filterDataUseCase;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void onViewAttached(final PriceView view) {
        this.view = view;
        if (list.isEmpty()) {
            fetchPrices();
        } else {
            setListAndHideSpinner(list);
        }
    }

    @Override
    public void onViewDetached() {
        this.view = null;
        disposeSubscription(filterSubscription);
        filterSubscription = null;
    }

    @Override
    public void onDestroyed() {
        disposeSubscription(fetchSubscription);
        fetchSubscription = null;
    }

    private void disposeSubscription(Disposable subscription) {
        if (subscription != null) {
            subscription.dispose();
        }
    }

    @VisibleForTesting
    protected void onPricesReceived(final List<Price> prices) {
        disposeSubscription(fetchSubscription);
        if (prices.isEmpty()) {
            onError("No items");
            return;
        }
        list = prices;

        setListAndHideSpinner(prices);
    }

    private void setListAndHideSpinner(List<Price> prices) {
        if (view == null) {
            return;
        }
//        view.setData(prices);
        view.setChartData(toLineSet(prices));
        view.hideLoadingSpinner();
        }

    private LineSet toLineSet(List<Price> prices) {
        LineSet set = new LineSet();

        FilterChartDataUseCase.Param param = new FilterChartDataUseCase.Param();
        param.dateFormat = Mapper.MONTH_YEAR;
        param.prices =prices;
        param.valuesLimit = prices.size();
        param.maxDisplayedLabelsCount = 8;
        filterDataUseCase.execute(param)
                .subscribeOn(schedulerProvider.getIoScheduler())
                .observeOn(schedulerProvider.getMainScheduler())
                .subscribe(point -> set.addPoint(point.getLabel(), point.getValue()));

        return set;
    }

    @NonNull
    private void fetchPrices() {
        fetchSubscription = fetchPricesUseCase
                .execute(Calendar.getInstance())
                .subscribeOn(schedulerProvider.getIoScheduler())
                .observeOn(schedulerProvider.getMainScheduler())
                .subscribe(this::onPricesReceived, e -> onError(e.getMessage()));
    }

    private void onError(final String message) {
        if (view == null) {
            return;
        }
        view.hideLoadingSpinner();
        view.displayErrorMessage(message);
    }

}
