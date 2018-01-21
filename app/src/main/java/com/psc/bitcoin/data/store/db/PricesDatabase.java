package com.psc.bitcoin.data.store.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PriceEntity.class}, version = 1, exportSchema = false)
public abstract class PricesDatabase extends RoomDatabase {

    public abstract PriceDao priceDao();

    private static final String DB_NAME = "Bitcoin.db";

    private static volatile PricesDatabase INSTANCE;

    public static PricesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PricesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PricesDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
