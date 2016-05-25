package com.trade.service;


import java.util.Optional;

/**
 * The request for quote interface
 */
public interface RfQService {
    Optional<Quote> quoteFor(String currency, int amount);

}
