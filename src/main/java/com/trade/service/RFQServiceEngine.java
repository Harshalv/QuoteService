package com.trade.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the RFQService interface. Provides a mechanism to get a quotes for bitcoins
 */
public class RFQServiceEngine implements RfQService {

    private static Logger log = Logger.getLogger("RFQServiceEngine");

    private LiveOrderBoard liveOrderBoard; //reference to the live order board

    private static final BigDecimal commission = new BigDecimal(0.02); // Our commission

    public RFQServiceEngine(LiveOrderBoard liveOrderBoard) {
        // We have a depencency on the live order board
        this.liveOrderBoard = liveOrderBoard;
    }

    /**
     *  Returns a quote
     * @param currency - The type of currency to trade the bitcoins in
     * @param amount - The number of bitcoins needed
     * @return -  A quote containing the ask and bid price if we can exactly match the request, else an empty quote
     */
    public Optional<Quote> quoteFor(String currency, int amount) {
        try {
            // No use going any further if we don't have a liveorder board OR the amount needed is not positive
            if (liveOrderBoard != null && amount > 0) {
                // have the whole code in a try catch block , so if the below conversion to currency goes tits up,
                // we log the error and send a empty quote
                final TradeCurrency cur = TradeCurrency.valueOf(currency);
                List<Order> orders = liveOrderBoard.ordersFor(currency);

                Optional<Order> sell = getLowestSellOrder(amount, cur, orders);

                Optional<Order> buy = getHighestBuyOrder(amount, cur, orders);

                if (buy.isPresent() && sell.isPresent()) {
                    // We deal with price as BigDecimal, but the Quote object needs a double
                    double bid = buy.get().getPrice().subtract(commission).doubleValue();
                    double ask = sell.get().getPrice().add(commission).doubleValue();
                    return Optional.of(new Quote(bid, ask));
                }
            }
            // If we don't find any exact matching Buy or Sell orders
            return Optional.empty();

        } catch (IllegalArgumentException e) {
            //Since the signature of quoteFor does not have a throws exception, we return a empty quote.
            // Logs should be able to highlight that the error occurred
            log.log(Level.SEVERE,"Illegal argument passed to quote for",e);
            return Optional.empty();
        } catch (Exception e) {
            log.log(Level.SEVERE,"Exception occurred in quote for",e);
            return Optional.empty();
        }
    }

    /**
     *  Helper method to get the Highest buy order from the order book
     * @param quantity - No of bitcoins requested
     * @param cur - the currency to trade in
     * @param orders - The list of orders from the live order board
     * @return - the highest buy order for the {cur} and {amount}
     */
    private Optional<Order> getHighestBuyOrder(int quantity, TradeCurrency cur, List<Order> orders) {
        return orders.stream().
                            filter(order -> cur.equals(order.getTradeCurrency()) &&
                                    TradeDirection.BUY.equals(order.getTradeDirection()) &&
                                    quantity == order.getQuantity()).
                            max((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()));
    }



    /**
     *  Helper method to get the lowest sell order from the order book
     * @param quantity - No of bitcoins requested
     * @param cur - the currency to trade in
     * @param orders - The list of orders from the live order board
     * @return - the lowest sell order for the {cur} and {amount}
     */
    private Optional<Order> getLowestSellOrder(int quantity, TradeCurrency cur, List<Order> orders) {
        return orders.stream().
                            filter(order -> cur.equals(order.getTradeCurrency()) &&
                                    TradeDirection.SELL.equals(order.getTradeDirection()) &&
                                    quantity == order.getQuantity()).
                            min((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()));

    }
}
