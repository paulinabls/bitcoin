package com.psc.bitcoin.data.store.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Model class for Price
 */
@Entity(tableName = "prices", indices = {@Index(value = "unixTime", unique = true)})
public class PriceEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long unixTime;

    private float value;

    PriceEntity(long unixTime, float value) {
        this.unixTime = unixTime;
        this.value = value;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public float getValue() {
        return value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
