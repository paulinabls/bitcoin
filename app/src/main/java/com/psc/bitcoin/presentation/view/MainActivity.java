package com.psc.bitcoin.presentation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.psc.bitcoin.R;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.presentation.presenter.PricePresenter;
import com.psc.bitcoin.presentation.presenter.PriceView;
import com.psc.bitcoin.presentation.presenter.base.PresenterFactory;
import com.psc.bitcoin.presentation.view.adapter.PriceAdapter;

import java.util.List;

public class MainActivity extends BaseActivity<PricePresenter, PriceView> implements PriceView {

    private static final int LOADER_ID = 0x007;
    private PriceAdapter adapter;
    private PricePresenter presenter;
    private View loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        setupTopControls();
    }

    private void setupTopControls() {
        loadingSpinner = findViewById(R.id.loading_spinner);
    }

    private void setupRecyclerView() {
        adapter = new PriceAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
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
    protected int getLoaderId() {
        return LOADER_ID;
    }

    public void setData(final List<Price> list) {
        adapter.setData(list);
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
