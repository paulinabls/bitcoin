package com.psc.bitcoin.data.store.db;

import com.psc.bitcoin.domain.model.Price;

import java.util.ArrayList;
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

    public List<Price> fromEntities(List<PriceEntity> priceEntities) {
        List<Price> prices = new ArrayList<>(priceEntities.size());
        for (PriceEntity entity : priceEntities) {
            prices.add(map(entity));
        }
        return prices;
    }

    private Price map(PriceEntity entity) {
        return new Price(entity.getUnixTime(), entity.getValue());
    }
}
