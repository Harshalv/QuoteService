package com.trade.service;

import java.util.List;

/**
 * The live order board interface
 */
public interface LiveOrderBoard {
    List<Order> ordersFor(String currency);
}
