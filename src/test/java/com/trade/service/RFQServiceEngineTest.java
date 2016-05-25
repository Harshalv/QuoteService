package com.trade.service;

import javafx.beans.binding.When;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Luther on 15/04/2016.
 */
public class RFQServiceEngineTest {

    LiveOrderBoard liveOrderBoard;
    @Before
    public void setUp() throws Exception {
        liveOrderBoard= Mockito.mock(LiveOrderBoard.class);

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(TradeDirection.BUY,TradeCurrency.USD,232.71f,200));
        orders.add(new Order(TradeDirection.SELL,TradeCurrency.USD,232.74,100));
        orders.add(new Order(TradeDirection.SELL,TradeCurrency.USD,232.73,200));
        orders.add(new Order(TradeDirection.BUY,TradeCurrency.USD,232.71,500));
        orders.add(new Order(TradeDirection.BUY,TradeCurrency.USD,232.70,100));
        orders.add(new Order(TradeDirection.SELL,TradeCurrency.USD,232.75,200));
        orders.add(new Order(TradeDirection.BUY,TradeCurrency.USD,232.69,500));
        orders.add(new Order(TradeDirection.SELL,TradeCurrency.USD,232.76,300));
        orders.add(new Order(TradeDirection.BUY,TradeCurrency.USD,232.70,200));
        orders.add(new Order(TradeDirection.SELL,TradeCurrency.GBP,232.76,300));
        orders.add(new Order(TradeDirection.BUY,TradeCurrency.GBP,232.70,300));

        when(liveOrderBoard.ordersFor(anyString())).thenReturn(orders);

    }

    @Test
    public void quoteForShouldReturnCorrectQuote() throws Exception {
        RfQService rfQService = new RFQServiceEngine(liveOrderBoard);

        final Optional<Quote> quote = rfQService.quoteFor("USD",200);
        Assert.assertTrue(quote.isPresent());
        if(quote.isPresent())
        {
            assertEquals(232.75,quote.get().ask);
            assertEquals(232.69,quote.get().bid);
        }
    }


    @Test
    public void quoteForShouldReturnCorrectQuoteFor100Bitcoins() throws Exception {
        RfQService rfQService = new RFQServiceEngine(liveOrderBoard);

        final Optional<Quote> quote = rfQService.quoteFor("USD",100);
        Assert.assertTrue(quote.isPresent());
        if(quote.isPresent())
        {
            assertEquals(232.76,quote.get().ask);
            assertEquals(232.68,quote.get().bid);
        }
    }


    @Test
    public void quoteForShouldReturnCorrectQuoteFor300BitcoinsInGBP() throws Exception {
        RfQService rfQService = new RFQServiceEngine(liveOrderBoard);

        final Optional<Quote> quote = rfQService.quoteFor("GBP",300);
        Assert.assertTrue(quote.isPresent());
        if(quote.isPresent())
        {
            assertEquals(232.78,quote.get().ask);
            assertEquals(232.68,quote.get().bid);
        }
    }

    @Test
    public void quoteForNotExactQuantityMatch() throws Exception {
        RfQService rfQService = new RFQServiceEngine(liveOrderBoard);
        final Optional<Quote> quote = rfQService.quoteFor("USD",500);
        Assert.assertFalse(quote.isPresent());
    }

    @Test
    public void quoteForWrongCurrency() throws Exception {
        RfQService rfQService = new RFQServiceEngine(liveOrderBoard);
        final Optional<Quote> quote = rfQService.quoteFor("EUR",500);
        Assert.assertFalse(quote.isPresent());
    }

    @Test
    public void quoteForNegativeQuantity() throws Exception {
        RfQService rfQService = new RFQServiceEngine(liveOrderBoard);
        final Optional<Quote> quote = rfQService.quoteFor("USD",-500);
        Assert.assertFalse(quote.isPresent());
    }

    @Test
    public void quoteForZeroQuantity() throws Exception {
        RfQService rfQService = new RFQServiceEngine(liveOrderBoard);
        final Optional<Quote> quote = rfQService.quoteFor("USD",0);
        Assert.assertFalse(quote.isPresent());
    }

}
