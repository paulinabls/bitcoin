package com.psc.bitcoin.presentation.presenter;

import com.db.chart.model.LineSet;
import com.psc.bitcoin.domain.model.Price;

import java.util.List;

public interface PriceView {
    void setData(List<Price> list);

    void setChartData(LineSet set);

    void showLoadingSpinner();

    void hideLoadingSpinner();

    void displayErrorMessage(String message);

}
