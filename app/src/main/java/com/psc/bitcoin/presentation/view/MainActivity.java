package com.psc.bitcoin.presentation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;
import com.psc.bitcoin.R;
import com.psc.bitcoin.presentation.model.LabeledValue;
import com.psc.bitcoin.presentation.model.Mapper;
import com.psc.bitcoin.presentation.presenter.PricePresenter;
import com.psc.bitcoin.presentation.presenter.PriceView;
import com.psc.bitcoin.presentation.presenter.base.PresenterFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<PricePresenter, PriceView> implements PriceView {

    private static final int LOADER_ID = 0x007;
    private PricePresenter presenter;
    private View loadingSpinner;
    private LineChartView chartView;
    private Spinner spinner;
    private int[] rangeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupChart();
        setupTopControls();
    }

    private void setupChart() {
        chartView = findViewById(R.id.linechart);
        chartView.setYLabels(AxisRenderer.LabelPosition.INSIDE);
    }

    private void setupTopControls() {
        loadingSpinner = findViewById(R.id.loading_spinner);
        spinner = findViewById(R.id.range_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.range_array, android.R.layout.simple_spinner_item);
        rangeArray = getResources().getIntArray(R.array.range_values_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @NonNull
    @Override
    protected PresenterFactory<PricePresenter> getPresenterFactory() {
        return new PricePresenterFactory();
    }

    @Override
    protected void onPresenterLoaded(@NonNull final PricePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinnerListener();
    }

    private void setSpinnerListener() {
        spinner.post(() -> spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onRangeSelected(rangeArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        }));
    }

    @Override
    protected void onPause() {
        spinner.setOnItemSelectedListener(null);
        super.onPause();
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }

    @Override
    public void setChartData(List<LabeledValue> valueList) {
        LineSet set = Mapper.toLineSet(valueList);
        set.setThickness(3);
        set.setColor(getResources().getColor(R.color.colorPrimary));
        ArrayList<ChartSet> data = new ArrayList<>();
        data.add(set);

        chartView.reset();
        chartView.addData(data);
        chartView.notifyDataUpdate();
        chartView.show();
    }

    @Override
    public void showLoadingSpinner() {
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingSpinner() {
        loadingSpinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayErrorMessage(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

}
