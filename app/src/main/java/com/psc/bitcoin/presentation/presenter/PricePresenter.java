package com.psc.bitcoin.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.usecase.FetchPricesUseCase;
import com.psc.bitcoin.presentation.presenter.base.Presenter;

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

    public PricePresenter(FetchPricesUseCase fetchPricesUseCase, SchedulerProvider schedulerProvider) {
        this.fetchPricesUseCase = fetchPricesUseCase;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void onViewAttached(final PriceView view) {
        this.view = view;
        if (list.isEmpty()) {
            fetchPrices();
        }
        else {
            view.setData(list);
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
        view.setData(prices);
        view.hideLoadingSpinner();
    }

    @NonNull
    private void fetchPrices() {
        fetchSubscription = fetchPricesUseCase
                .execute(null)
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
