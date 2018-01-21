package com.psc.bitcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.psc.bitcoin.data.network.BlockchainApi;
import com.psc.bitcoin.data.network.HttpClientProvider;
import com.psc.bitcoin.data.network.ServiceProvider;
import com.psc.bitcoin.data.store.LocalStorage;
import com.psc.bitcoin.data.store.Storage;
import com.psc.bitcoin.data.store.ValueStorage;
import com.psc.bitcoin.data.store.db.EntityMapper;
import com.psc.bitcoin.data.store.db.PriceDao;
import com.psc.bitcoin.data.store.db.PricesDatabase;

import okhttp3.OkHttpClient;

public class Injection {
    public static Storage provideStorage(Context context) {
        PriceDao priceDao = PricesDatabase.getInstance(context).priceDao();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        ValueStorage valueStorage = new ValueStorage(prefs);
        return new LocalStorage(priceDao, valueStorage, new EntityMapper());
    }

    public static BlockchainApi provideBlockchainService(Context context) {
        final OkHttpClient httpClient = new HttpClientProvider().create(context);
        return ServiceProvider.createRestService(httpClient);
    }
}
