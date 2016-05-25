package com.trade.service;

/**
 * Enum representing a the trade currency used currently in quotes and orders
 */
public enum TradeCurrency {
    USD("USD"),GBP("GBP");

    private String currency;

    TradeCurrency(String cur) {
        this.currency = cur;
    }
}
