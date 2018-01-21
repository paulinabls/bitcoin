package com.psc.bitcoin.presentation.presenter.base;

public interface Presenter<V> {
    void onViewAttached(V view);

    void onViewDetached();

    void onDestroyed();
}
