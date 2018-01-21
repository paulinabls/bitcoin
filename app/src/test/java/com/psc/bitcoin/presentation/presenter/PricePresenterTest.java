package com.psc.bitcoin.presentation.presenter;

import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.usecase.FetchPricesUseCase;
import com.psc.bitcoin.domain.usecase.FilterChartDataUseCase;
import com.psc.bitcoin.presentation.model.LabeledValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PricePresenterTest {

    private final static List<Price> PRICES = Arrays.asList(new Price(1, 2.0f));
    private static final Observable<List<Price>> OBSERVABLE_LIST = Observable.just(PRICES);
    public static final LabeledValue LABELED_VALUE = new LabeledValue("das", 23);

    @Mock
    private FetchPricesUseCase fetchPricesUseCase;
    @Mock
    private PriceView view;

    @Mock
    private FilterChartDataUseCase filterDataUseCase;

    @Mock
    SchedulerProvider schedulerProvider;

    private PricePresenter tested;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tested = new PricePresenter(fetchPricesUseCase, filterDataUseCase, schedulerProvider);
        when(fetchPricesUseCase.execute(any())).thenReturn(OBSERVABLE_LIST);
        when(schedulerProvider.getMainScheduler()).thenReturn(Schedulers.trampoline());
        when(schedulerProvider.getIoScheduler()).thenReturn(Schedulers.trampoline());
        when(filterDataUseCase.execute(any())).thenReturn(Observable.just(LABELED_VALUE));
    }

    @Test
    public void onViewAttached_whenViewReattached_shouldSetData() throws Exception {
        tested.onViewAttached(view);
        tested.onViewDetached();
        clearInvocations(view);

        tested.onViewAttached(view);

        verify(view).setChartData(any());
    }

    @Test
    public void onViewAttached_whenNotEmptyListFetched_informsView() throws Exception {
        tested.onViewAttached(view);

        verify(view).setChartData(any());
        verify(view).hideLoadingSpinner();
    }

    @Test
    public void onPricesReceived_whenEmptyList_andWhenViewNotAttached_shouldNotInformView() throws Exception {
        tested.onPricesReceived(Collections.emptyList());

        verify(view, Mockito.never()).hideLoadingSpinner();
        verify(view, Mockito.never()).displayErrorMessage(anyString());
    }

    @Test
    public void onPricesReceived_whenNotEmptyList_andWhenViewNotAttached_shouldNotInformView() throws Exception {
        tested.onPricesReceived(PRICES);

        verify(view, Mockito.never()).setChartData(any());
        verify(view, Mockito.never()).hideLoadingSpinner();
    }

}