package com.psc.bitcoin.presentation.presenter;

import com.psc.bitcoin.domain.model.Price;

import java.util.List;

public interface PriceView {
    void setData(List<Price> list);

    void showLoadingSpinner();

    void hideLoadingSpinner();

    void displayErrorMessage(String message);

}
