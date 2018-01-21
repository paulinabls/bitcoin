package com.psc.bitcoin.data.network;

import com.psc.bitcoin.data.model.MarketPriceResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BlockchainApi {

    @GET("charts/market-price")
    Observable<MarketPriceResponse> getPrices(@Query("timespan") String timespan, @Query("format") String format);

}
