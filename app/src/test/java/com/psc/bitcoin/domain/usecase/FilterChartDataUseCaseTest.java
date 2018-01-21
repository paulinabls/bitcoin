package com.psc.bitcoin.domain.usecase;

import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.domain.usecase.FilterChartDataUseCase.Param;
import com.psc.bitcoin.presentation.model.LabeledValue;
import com.psc.bitcoin.presentation.model.Mapper;
import com.psc.bitcoin.presentation.model.MapperFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilterChartDataUseCaseTest {

    public static final Price PRICE = new Price(2, 3f);
    @Mock
    MapperFactory mapperFactory;
    @Mock
    Mapper mapper;


    private FilterChartDataUseCase tested;
    private Param param = new Param();
    private static final String FORMAT = "anyFormat";
    private static final LabeledValue SAMPLE_VALUE = new LabeledValue("label", 3.2f);
    private static final LabeledValue SAMPLE_EMPTY_LABEL = new LabeledValue("", 3.2f);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tested = new FilterChartDataUseCase(mapperFactory);

        when(mapperFactory.createWith(any())).thenReturn(mapper);
        when(mapper.withDateLabel(any())).thenReturn(SAMPLE_VALUE);
        when(mapper.withEmptyLabel(any())).thenReturn(SAMPLE_EMPTY_LABEL);
        param.dateFormat = FORMAT;
        param.prices = Arrays.asList(PRICE, PRICE, PRICE);

    }

    @Test
    public void createsMapperUsingDateFormatPassedInParam() throws Exception {
        param.prices = Arrays.asList(PRICE, PRICE, PRICE);
        tested.execute(param);

        verify(mapperFactory).createWith(FORMAT);
    }

    @Test
    public void limitsReturnedList_toLimitValue() throws Exception {
        param.valuesLimit = 1;

        final TestObserver<LabeledValue> test = tested.execute(param).test();

        test.assertValueCount(1);
    }

    @Test
    public void valuesWithLabels_ifMaxDisplayedLabelsCount_equalsToValuesLimit_shouldOnlyCreateWithLabels() throws Exception {
        param.valuesLimit = 3;
        param.maxDisplayedLabelsCount = 3;

        final TestObserver<LabeledValue> test = tested.execute(param).test();

        test.assertValueCount(3);
        verify(mapper, times(3)).withDateLabel(any());
        verify(mapper, never()).withEmptyLabel(any());
    }

}