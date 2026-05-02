package riskstream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the PriceEvent class.
 */
public class TestPriceEvent {

    /**
     * Verifies that the constructor stores all price event fields.
     */
    @Test
    void constructorStoresPriceEventFields() {
        PriceEvent priceEvent = new PriceEvent("AAPL", 25.0);

        assertEquals("AAPL", priceEvent.getSymbol());
        assertEquals(25.0, priceEvent.getPrice());
    }

    /**
     * Verifies that setter methods update all price event fields.
     */
    @Test
    void settersUpdatePriceEventFields() {
        PriceEvent priceEvent = new PriceEvent("AAPL", 25.0);

        priceEvent.setSymbol("MSFT");
        priceEvent.setPrice(40.0);

        assertEquals("MSFT", priceEvent.getSymbol());
        assertEquals(40.0, priceEvent.getPrice());
    }
}
