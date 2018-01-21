package com.psc.bitcoin.data.mapper;

import android.support.annotation.Nullable;

import com.psc.bitcoin.data.model.MarketPriceResponse;
import com.psc.bitcoin.data.model.Value;
import com.psc.bitcoin.domain.model.Price;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceMapper {

    public List<Price> map(@Nullable final MarketPriceResponse response) {
        if (response == null || !response.isStatusOK()) {
            return Collections.emptyList();
        }

        List<Value> values = response.getValues();
        List<Price> list = new ArrayList<>(values.size());
        for (Value value : values) {
            list.add(mapFromApi(value));
        }
        return list;
    }

    private Price mapFromApi(Value value) {
        return new Price(value.getX(), value.getY());
    }
}
