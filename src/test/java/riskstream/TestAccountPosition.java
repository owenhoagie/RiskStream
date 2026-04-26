package riskstream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the AccountPosition class, including account ID storage and exposure updates.
 */
public class TestAccountPosition {

    /**
     * Verifies that a new AccountPosition starts with zero total exposure.
     */
    @Test
    void newAccountPositionStartsWithZeroExposure() {
        AccountPosition position = new AccountPosition("ACC1");

        assertEquals(0.0, position.getTotalExposure());
    }

    /**
     * Verifies that the constructor stores the provided account ID.
     */
    @Test
    void constructorStoresAccountId() {
        AccountPosition position = new AccountPosition("ACC1");

        assertEquals("ACC1", position.getAccountId());
    }

    /**
     * Verifies that setAccountId changes the account ID.
     */
    @Test
    void setAccountIdUpdatesAccountId() {
        AccountPosition position = new AccountPosition("ACC1");

        position.setAccountId("ACC2");

        assertEquals("ACC2", position.getAccountId());
    }

    /**
     * Verifies that adding one trade increases total exposure by that trade's value.
     */
    @Test
    void addTradeIncreasesTotalExposure() {
        AccountPosition position = new AccountPosition("ACC1");
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 10, 25.0);

        position.addTrade(trade);

        assertEquals(250.0, position.getTotalExposure());
    }

    /**
     * Verifies that multiple trades are accumulated into one total exposure value.
     */
    @Test
    void addTradeAccumulatesMultipleTradeValues() {
        AccountPosition position = new AccountPosition("ACC1");
        TradeEvent firstTrade = new TradeEvent("ACC1", "AAPL", 10, 25.0);
        TradeEvent secondTrade = new TradeEvent("ACC1", "MSFT", 5, 40.0);

        position.addTrade(firstTrade);
        position.addTrade(secondTrade);

        assertEquals(450.0, position.getTotalExposure());
    }
}
