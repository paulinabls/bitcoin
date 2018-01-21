package com.psc.bitcoin.data;

import com.psc.bitcoin.data.mapper.PriceMapper;
import com.psc.bitcoin.data.network.BlockchainApi;
import com.psc.bitcoin.data.store.Storage;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BitcoinRepositoryTest {
    private final static String TIMESPAN = "30days";
    private static final String JSON = "json";

    @Mock
    BlockchainApi api;
    @Mock
    PriceMapper priceMapper;
    @Mock
    SchedulerProvider schedulerProvider;
    @Mock
    Storage storage;

    private BitcoinRepository tested;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tested = new BitcoinRepository(api, storage, priceMapper, schedulerProvider);
    }

//    @Test
//    public void getPrices_WhenSubscribed() throws Exception {
//        Price price = new Price(2, "name", "DEscription", "imgUrl", "firstBrewed");
//        Observable<List<Price>> observable = Observable.just(Arrays.asList(price));
//        when(api.getPrices(TIMESPAN, JSON)).thenReturn(observable);
//        when(schedulerProvider.getIoScheduler()).thenReturn(Schedulers.trampoline());
//        when(schedulerProvider.getMainScheduler()).thenReturn(Schedulers.trampoline());
//
//        final Observable<List<Price>> listObservable = tested.fetchPrices(TIMESPAN);
//        listObservable.subscribe();
//
//        verify(api).getPrices(TIMESPAN, JSON);
//        verify(priceMapper).map(any());
//        verify(schedulerProvider).getIoScheduler();
//        verify(schedulerProvider).getMainScheduler();
//    }

}