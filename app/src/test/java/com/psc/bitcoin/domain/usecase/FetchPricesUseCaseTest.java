package com.psc.bitcoin.domain.usecase;

import com.psc.bitcoin.data.SchedulerProvider;
import com.psc.bitcoin.domain.Repository;
import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.utils.CalendarUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FetchPricesUseCaseTest {

    private static final Calendar currentDate = Calendar.getInstance();
    private static final List<Price> TWO_ITEMS_LIST = Arrays.asList(new Price(4, 3f), new Price(2, 3f));
    private static final List<Price> ONE_ITEM_PRICE_LIST = Arrays.asList(new Price(1, 2f));
    @Mock
    Repository repository;
    @Mock
    CalendarUtils calendarUtils;
    @Mock
    SchedulerProvider schedulerProvider;
    private FetchPricesUseCase tested;
    private Calendar lastFetchedDate;
    private Observable<List<Price>> listObservable;
    private Observable<List<Price>> allStoredPrices = Observable.empty();
    private Observable<List<Price>> fetchedPrices;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        tested = new FetchPricesUseCase(repository, calendarUtils, schedulerProvider);
        allStoredPrices = Observable.empty();
    }

    private void setUpRepository() {
        when(repository.getLastFetchedDate()).thenReturn(lastFetchedDate);
        when(repository.fetchAllPrices()).thenReturn(listObservable);
        when(repository.getAllPrices()).thenReturn(allStoredPrices);
        when(repository.fetchPrices(anyInt())).thenReturn(fetchedPrices);
    }

    @Test
    public void whenLastFetchedDateIsNull_shouldFetchAllPrices_storeThem_andStoreDate() throws Exception {
        lastFetchedDate = null;
        listObservable = Observable.just(ONE_ITEM_PRICE_LIST);

        setUpRepository();

        tested.execute(currentDate).subscribe();

        verify(repository).getLastFetchedDate();
        verify(repository).fetchAllPrices();
        verify(repository).storePrices(ONE_ITEM_PRICE_LIST);
        verify(repository).storeLastFetchedDate(currentDate);
        verify(repository).getAllPrices();
    }

    @Test
    public void whenLastFetchedDateIsYesterday_shouldFetchPrices_storeThem_andStoreDate() throws Exception {
        lastFetchedDate = Calendar.getInstance();
        fetchedPrices = Observable.just(TWO_ITEMS_LIST);

        setUpRepository();
        when(calendarUtils.calculateDifferenceInDays(currentDate, lastFetchedDate)).thenReturn(1);

        tested.execute(currentDate).subscribe();

        verify(calendarUtils).calculateDifferenceInDays(currentDate, lastFetchedDate);
        verify(repository).getLastFetchedDate();
        verify(repository).fetchPrices(1);
        verify(repository).storePrices(TWO_ITEMS_LIST);
        verify(repository).storeLastFetchedDate(currentDate);
        verify(repository).getAllPrices();
    }

    @Test
    public void whenLastFetchedDateIsSameAsCurrent_shouldGetAllPrices_andNotStoreData() throws Exception {
        lastFetchedDate = Calendar.getInstance();
        fetchedPrices = Observable.just(ONE_ITEM_PRICE_LIST);

        setUpRepository();
        when(calendarUtils.calculateDifferenceInDays(currentDate, lastFetchedDate)).thenReturn(0);

        tested.execute(currentDate).subscribe();

        verify(repository).getLastFetchedDate();
        verify(calendarUtils).calculateDifferenceInDays(currentDate, lastFetchedDate);
        verify(repository, never()).storePrices(anyList());
        verify(repository, never()).storeLastFetchedDate(currentDate);
        verify(repository).getAllPrices();
    }
}