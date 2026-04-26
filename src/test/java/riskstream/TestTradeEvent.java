package riskstream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the TradeEvent class, including field access and trade value calculation.
 */
public class TestTradeEvent {

    /**
     * Verifies that the constructor stores all trade fields.
     */
    @Test
    void constructorStoresTradeFields() {
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 10, 25.0);

        assertEquals("ACC1", trade.getAccountId());
        assertEquals("AAPL", trade.getSymbol());
        assertEquals(10, trade.getQuantity());
        assertEquals(25.0, trade.getPrice());
    }

    /**
     * Verifies that the setter methods update all trade fields.
     */
    @Test
    void settersUpdateTradeFields() {
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 10, 25.0);

        trade.setAccountId("ACC2");
        trade.setSymbol("MSFT");
        trade.setQuantity(5);
        trade.setPrice(40.0);

        assertEquals("ACC2", trade.getAccountId());
        assertEquals("MSFT", trade.getSymbol());
        assertEquals(5, trade.getQuantity());
        assertEquals(40.0, trade.getPrice());
    }

    /**
     * Verifies that trade value is calculated as quantity multiplied by price.
     */
    @Test
    void calculateTradeValueMultipliesQuantityByPrice() {
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 10, 25.0);

        assertEquals(250.0, trade.calculateTradeValue());
    }

    /**
     * Verifies that trade value is positive even when the quantity is negative.
     */
    @Test
    void calculateTradeValueReturnsPositiveValueForNegativeQuantity() {
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", -10, 25.0);

        assertEquals(250.0, trade.calculateTradeValue());
    }
}
