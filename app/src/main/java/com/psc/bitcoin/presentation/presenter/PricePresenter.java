package com.psc.bitcoin.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.db.chart.model.LineSet;
import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.usecase.FetchPricesUseCase;
import com.psc.bitcoin.domain.usecase.FilterChartDataUseCase;
import com.psc.bitcoin.presentation.model.LabeledValue;
import com.psc.bitcoin.presentation.model.Mapper;
import com.psc.bitcoin.presentation.presenter.base.Presenter;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PricePresenter implements Presenter<PriceView> {

    private PriceView view;
    private final FetchPricesUseCase fetchPricesUseCase;
    private final SchedulerProvider schedulerProvider;
    private Disposable fetchSubscription;
    private Disposable filterSubscription;
    private List<Price> list = Collections.emptyList();
    private FilterChartDataUseCase filterDataUseCase;
    private int recentRange = -1;

    public PricePresenter(FetchPricesUseCase fetchPricesUseCase, FilterChartDataUseCase filterDataUseCase, SchedulerProvider schedulerProvider) {
        this.fetchPricesUseCase = fetchPricesUseCase;
        this.filterDataUseCase = filterDataUseCase;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void onViewAttached(final PriceView view) {
        this.view = view;
        if (list.isEmpty()) {
            view.showLoadingSpinner();
            fetchPrices();
        } else {
            setDataAndHideSpinner(list);
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

        setDataAndHideSpinner(prices);
    }

    private void setDataAndHideSpinner(List<Price> prices) {
        if (view == null) {
            return;
        }
        filterRange(prices, recentRange);
    }

    private void filterRange(List<Price> prices, int rangeLimit) {
        FilterChartDataUseCase.Param param = new FilterChartDataUseCase.Param();
        param.valuesLimit = rangeLimit == -1 ? list.size() : rangeLimit;
        param.dateFormat = param.valuesLimit > 30 ? Mapper.MONTH_YEAR : Mapper.DAY_MONTH;
        param.prices = prices;
        param.maxDisplayedLabelsCount = 8;
        filter(param);
    }

    private void filter(FilterChartDataUseCase.Param param) {
        filterSubscription = filterDataUseCase.execute(param)
                .subscribeOn(schedulerProvider.getIoScheduler())
                .observeOn(schedulerProvider.getMainScheduler())
                .toList()
                .subscribe(this::setChartDataAndHideSpinner, e -> onError(e.getMessage()));
    }

    private void setChartDataAndHideSpinner(List<LabeledValue> list) {
        if (view == null) {
            return;
        }
        view.setChartData(list);
        view.hideLoadingSpinner();
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

    /**
     * when -1 passed all data will be displayed
     *
     * @param range in days to be displayed
     */
    public void onRangeSelected(int range) {
        if (list.isEmpty()) {
            return;
        }
        view.showLoadingSpinner();

        recentRange = range;
        filterRange(list, range);
    }
}
