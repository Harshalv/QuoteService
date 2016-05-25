package com.trade.service;

/**
 * * Enum representing the trade direction.
 */
public enum TradeDirection {
    BUY("BUY"),SELL("SELL");

    private String direction;

    TradeDirection(String direction) {
        this.direction = direction;
    }
}
