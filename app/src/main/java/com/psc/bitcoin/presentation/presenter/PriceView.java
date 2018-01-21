package com.psc.bitcoin.presentation.presenter;

import com.psc.bitcoin.presentation.model.LabeledValue;

import java.util.List;

public interface PriceView {

    void setChartData(List<LabeledValue> valueList);

    void showLoadingSpinner();

    void hideLoadingSpinner();

    void displayErrorMessage(String message);

}
