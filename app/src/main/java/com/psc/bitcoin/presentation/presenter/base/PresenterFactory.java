package com.psc.bitcoin.presentation.presenter.base;


import android.content.Context;

public interface PresenterFactory<T extends Presenter> {
    T create(Context context);
}
