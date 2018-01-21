package com.psc.bitcoin.data.store.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Interface for database access for prices.
 */
@Dao
public interface PriceDao {

    @Query("SELECT * FROM prices")
    Flowable<List<PriceEntity>> getPrices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(PriceEntity... items);

}
