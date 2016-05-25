package com.trade.service;

import java.math.BigDecimal;

/**
 * Class representing a Order
 */
public class Order {
    private TradeDirection tradeDirection;
    private TradeCurrency tradeCurrency;
    private BigDecimal price;
    private Integer quantity;

    public Order(TradeDirection tradeDirection, TradeCurrency tradeCurrency, double price, int quantity) {
        this.tradeDirection = tradeDirection;
        this.tradeCurrency = tradeCurrency;
        this.price = BigDecimal.valueOf(price);
        this.quantity = new Integer(quantity);
    }

    public TradeDirection getTradeDirection() {
        return tradeDirection;
    }

    public TradeCurrency getTradeCurrency() {
        return tradeCurrency;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
