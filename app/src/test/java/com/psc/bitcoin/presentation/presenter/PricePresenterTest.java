package com.psc.bitcoin.presentation.presenter;

import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.usecase.FetchPricesUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PricePresenterTest {

    private final static List<Price> PRICES = Arrays.asList(new Price(1, 2.0f));
    private static final Observable<List<Price>> OBSERVABLE_LIST = Observable.just(PRICES);

    @Mock
    private FetchPricesUseCase fetchPricesUseCase;
    @Mock
    private PriceView view;

    @Mock
    SchedulerProvider schedulerProvider;
    private PricePresenter tested;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tested = new PricePresenter(fetchPricesUseCase, schedulerProvider);
        when(fetchPricesUseCase.execute(null)).thenReturn(OBSERVABLE_LIST);
        when(schedulerProvider.getMainScheduler()).thenReturn(Schedulers.trampoline());
        when(schedulerProvider.getIoScheduler()).thenReturn(Schedulers.trampoline());
    }

    @Test
    public void onViewAttached_whenViewReattached_shouldSetData() throws Exception {
        tested.onViewAttached(view);
        tested.onViewDetached();
        clearInvocations(view);

        tested.onViewAttached(view);

        verify(view).setData(PRICES);
    }

    @Test
    public void onViewAttached_whenNotEmptyListFetched_informsView() throws Exception {
        tested.onViewAttached(view);

        verify(view).setData(PRICES);
        verify(view).hideLoadingSpinner();
    }

    @Test
    public void onViewAttached_whenEmptyListFetched_shouldShowError_andHideLoadingSpinner() throws Exception {
        when(fetchPricesUseCase.execute(null)).thenReturn(Observable.just(Collections.emptyList()));
        tested.onViewAttached(view);

        verify(view).hideLoadingSpinner();
        verify(view).displayErrorMessage(anyString());
    }

    @Test
    public void onPricesReceived_whenEmptyList_andWhenViewNotAttached_shouldNotInformView() throws Exception {
        tested.onPricesReceived(Collections.emptyList());

        verify(view, never()).hideLoadingSpinner();
        verify(view, never()).displayErrorMessage(anyString());
    }

    @Test
    public void onPricesReceived_whenNotEmptyList_andWhenViewNotAttached_shouldNotInformView() throws Exception {
        tested.onPricesReceived(PRICES);

        verify(view, never()).setData(PRICES);
        verify(view, never()).hideLoadingSpinner();
    }

}