package com.psc.bitcoin.presentation.model;

import java.text.SimpleDateFormat;

public class MapperFactory {
    public Mapper createWith(String dateFormat) {
        return new Mapper(new SimpleDateFormat(dateFormat));
    }
}
