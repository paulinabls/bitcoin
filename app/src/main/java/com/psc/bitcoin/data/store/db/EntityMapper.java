package com.psc.bitcoin.data.store.db;

import com.psc.bitcoin.domain.model.Price;

import java.util.List;

public class EntityMapper {

    public PriceEntity[] toEntities(List<Price> prices) {
        PriceEntity[] priceEntities = new PriceEntity[prices.size()];
        for (int i = 0; i < prices.size(); i++) {
            priceEntities[i] = map(prices.get(i));
        }

        return priceEntities;
    }

    private PriceEntity map(Price price) {
        return new PriceEntity(price.getUnixTime(), price.getValue());
    }
}
