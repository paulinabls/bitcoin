package com.psc.bitcoin;

import android.app.Application;

import com.psc.bitcoin.data.store.db.PricesDatabase;

public class BitcoinApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public PricesDatabase getDatabase() {
        return PricesDatabase.getInstance(this);
    }

}
