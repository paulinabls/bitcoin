package com.psc.bitcoin.presentation.view;

import android.content.Context;

import com.psc.bitcoin.Injection;
import com.psc.bitcoin.data.BitcoinRepository;
import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.data.mapper.PriceMapper;
import com.psc.bitcoin.data.network.BlockchainApi;
import com.psc.bitcoin.data.store.Storage;
import com.psc.bitcoin.domain.Repository;
import com.psc.bitcoin.domain.usecase.FetchPricesUseCase;
import com.psc.bitcoin.domain.utils.CalendarUtils;
import com.psc.bitcoin.presentation.presenter.PricePresenter;
import com.psc.bitcoin.presentation.presenter.base.PresenterFactory;

class PricePresenterFactory implements PresenterFactory<PricePresenter> {

    @Override
    public PricePresenter create(Context context) {
        final Context appContext = context.getApplicationContext();
        final BlockchainApi punkService = Injection.provideBlockchainService(appContext);
        final Storage storage = Injection.provideStorage(appContext);
        final SchedulerProvider schedulerProvider = new SchedulerProvider();
        final Repository repository = new BitcoinRepository(punkService, storage, new PriceMapper(), schedulerProvider);
        final CalendarUtils calendarUtils = new CalendarUtils();
        final FetchPricesUseCase fetchPricesUseCase = new FetchPricesUseCase(repository, calendarUtils, schedulerProvider);

        return new PricePresenter(fetchPricesUseCase, schedulerProvider);
    }

}
