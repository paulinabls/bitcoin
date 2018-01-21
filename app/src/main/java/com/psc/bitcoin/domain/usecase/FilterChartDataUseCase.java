package com.psc.bitcoin.domain.usecase;

import com.psc.bitcoin.domain.model.Price;
import com.psc.bitcoin.presentation.model.Mapper;
import com.psc.bitcoin.presentation.model.LabeledValue;
import com.psc.bitcoin.presentation.model.MapperFactory;

import java.util.List;

import io.reactivex.Observable;

public class FilterChartDataUseCase implements UseCase<FilterChartDataUseCase.Param, Observable<LabeledValue>> {

    private MapperFactory mapperFactory;

    public FilterChartDataUseCase(MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    @Override
    public Observable<LabeledValue> execute(Param param) {
        Mapper mapper = mapperFactory.createWith(param.dateFormat);
        int noLabelRange = param.valuesLimit / param.maxDisplayedLabelsCount;
        return Observable.fromIterable(param.prices)
                .sorted((p1, p2) -> Long.compare(p1.getUnixTime(), p2.getUnixTime()))
                .takeLast(param.valuesLimit)
                .zipWith(Observable.range(0, param.valuesLimit),
                        (price, integer) -> {
                            if (noLabelRange == 0 || (integer % noLabelRange) == 0) {
                                return mapper.withDateLabel(price);
                            }
                            return mapper.withEmptyLabel(price);
                        });
    }

    public static class Param {
        public List<Price> prices;
        public String dateFormat;
        public int valuesLimit;
        public int maxDisplayedLabelsCount = 1;
    }
}
