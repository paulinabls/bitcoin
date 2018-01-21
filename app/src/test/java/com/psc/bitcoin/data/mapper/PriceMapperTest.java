package com.psc.bitcoin.data.mapper;

import com.psc.bitcoin.data.model.Value;
import com.psc.bitcoin.domain.model.Price;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PriceMapperTest {

    private PriceMapper tested = new PriceMapper();

    @Test
    public void map_whenNullPassed_returnsEmptyList() throws Exception {
        final List<Price> beerInfos = tested.map(null);
        assertTrue(beerInfos.isEmpty());
    }

//    @Test
//    public void map_whenBeerListPassed_returnsItAsPriceList() throws Exception {
//        Beer beer1 = new Beer(11, "pale ale", "description", "imageUrl", "brewedDate" );
//        Beer beer2= new Beer(21, "dark beer", "another description", "second imageUrl", "a brewedDate" );
//
//        final List<Beer> list = Arrays.asList(beer1, beer2);
//
//        final List<Price> prices = tested.map(list);
//
//        assertEquals(2, prices.size());
//        verifyPriceEquals(beer1, prices.get(0));
//        verifyPriceEquals(beer2, prices.get(1));
//    }

    private void verifyPriceEquals(Value expectedValue, Price beerInfo) {
        assertEquals(expectedValue.getX(), beerInfo.getUnixTime());
        assertEquals(expectedValue.getY(), beerInfo.getValue());
    }

}