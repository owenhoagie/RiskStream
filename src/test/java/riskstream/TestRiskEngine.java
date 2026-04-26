package riskstream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the RiskEngine class, including trade validation, exposure tracking, and alert creation.
 */
public class TestRiskEngine {

    /**
     * Verifies that a new RiskEngine stores its max exposure and starts with no positions.
     */
    @Test
    void constructorStoresMaxExposureAndStartsWithNoPositions() {
        RiskEngine engine = new RiskEngine(1000);

        assertEquals(1000, engine.getMaxExposure());
        assertTrue(engine.getAccountPositions().isEmpty());
    }

    /**
     * Verifies that a complete trade with positive price and nonzero quantity is valid.
     */
    @Test
    void isValidTradeReturnsTrueForValidTrade() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 10, 25.0);

        assertTrue(engine.isValidTrade(trade));
    }

    /**
     * Verifies that a null trade is invalid.
     */
    @Test
    void isValidTradeReturnsFalseForNullTrade() {
        RiskEngine engine = new RiskEngine(1000);

        assertFalse(engine.isValidTrade(null));
    }

    /**
     * Verifies that a trade with a missing account ID is invalid.
     */
    @Test
    void isValidTradeReturnsFalseForMissingAccountId() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("", "AAPL", 10, 25.0);

        assertFalse(engine.isValidTrade(trade));
    }

    /**
     * Verifies that a trade with a blank symbol is invalid.
     */
    @Test
    void isValidTradeReturnsFalseForMissingSymbol() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("ACC1", " ", 10, 25.0);

        assertFalse(engine.isValidTrade(trade));
    }

    /**
     * Verifies that a trade with zero quantity is invalid.
     */
    @Test
    void isValidTradeReturnsFalseForZeroQuantity() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 0, 25.0);

        assertFalse(engine.isValidTrade(trade));
    }

    /**
     * Verifies that a trade with a zero or negative price is invalid.
     */
    @Test
    void isValidTradeReturnsFalseForNonPositivePrice() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 10, 0.0);

        assertFalse(engine.isValidTrade(trade));
    }

    /**
     * Verifies that a valid under-limit trade updates exposure without creating an alert.
     */
    @Test
    void processTradeUpdatesPositionAndReturnsNullWhenUnderLimit() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 10, 25.0);

        RiskAlert alert = engine.processTrade(trade);

        assertNull(alert);
        assertEquals(250.0, engine.getAccountPositions().get("ACC1").getTotalExposure());
    }

    /**
     * Verifies that multiple trades for the same account accumulate exposure.
     */
    @Test
    void processTradeAccumulatesExposureForSameAccount() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent firstTrade = new TradeEvent("ACC1", "AAPL", 10, 25.0);
        TradeEvent secondTrade = new TradeEvent("ACC1", "MSFT", 5, 40.0);

        engine.processTrade(firstTrade);
        engine.processTrade(secondTrade);

        assertEquals(450.0, engine.getAccountPositions().get("ACC1").getTotalExposure());
    }

    /**
     * Verifies that trades for different accounts are tracked in separate positions.
     */
    @Test
    void processTradeKeepsDifferentAccountsSeparate() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent firstTrade = new TradeEvent("ACC1", "AAPL", 10, 25.0);
        TradeEvent secondTrade = new TradeEvent("ACC2", "MSFT", 5, 40.0);

        engine.processTrade(firstTrade);
        engine.processTrade(secondTrade);

        assertEquals(250.0, engine.getAccountPositions().get("ACC1").getTotalExposure());
        assertEquals(200.0, engine.getAccountPositions().get("ACC2").getTotalExposure());
    }

    /**
     * Verifies that an invalid non-null trade returns an invalid event alert.
     */
    @Test
    void processTradeReturnsInvalidEventAlertForInvalidTrade() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("", "AAPL", 10, 25.0);

        RiskAlert alert = engine.processTrade(trade);

        assertNotNull(alert);
        assertEquals("INVALID_EVENT", alert.getAlertType());
        assertEquals("", alert.getAccountId());
        assertEquals("AAPL", alert.getSymbol());
        assertEquals(0.0, alert.getExposure());
        assertEquals(1000.0, alert.getLimit());
    }

    /**
     * Verifies that a null trade returns an invalid event alert with unknown identifiers.
     */
    @Test
    void processTradeReturnsInvalidEventAlertForNullTrade() {
        RiskEngine engine = new RiskEngine(1000);

        RiskAlert alert = engine.processTrade(null);

        assertNotNull(alert);
        assertEquals("INVALID_EVENT", alert.getAlertType());
        assertEquals("UNKNOWN", alert.getAccountId());
        assertEquals("UNKNOWN", alert.getSymbol());
    }

    /**
     * Verifies that a trade above the exposure limit returns an exposure breach alert.
     */
    @Test
    void processTradeReturnsExposureLimitAlertWhenExposureExceedsLimit() {
        RiskEngine engine = new RiskEngine(1000);
        TradeEvent trade = new TradeEvent("ACC1", "AAPL", 50, 25.0);

        RiskAlert alert = engine.processTrade(trade);

        assertNotNull(alert);
        assertEquals("EXPOSURE_LIMIT_BREACH", alert.getAlertType());
        assertEquals("ACC1", alert.getAccountId());
        assertEquals("AAPL", alert.getSymbol());
        assertEquals(1250.0, alert.getExposure());
        assertEquals(1000.0, alert.getLimit());
    }
}
