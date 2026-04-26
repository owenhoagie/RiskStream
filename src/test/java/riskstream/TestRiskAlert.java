package riskstream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the RiskAlert class, including alert field storage and updates.
 */
public class TestRiskAlert {

    /**
     * Verifies that the constructor stores all alert fields.
     */
    @Test
    void constructorStoresAlertFields() {
        RiskAlert alert = new RiskAlert(
            "ACC1",
            "AAPL",
            "EXPOSURE_LIMIT_BREACH",
            "Account position exceeds max exposure limit.",
            1200.0,
            1000.0
        );

        assertEquals("ACC1", alert.getAccountId());
        assertEquals("AAPL", alert.getSymbol());
        assertEquals("EXPOSURE_LIMIT_BREACH", alert.getAlertType());
        assertEquals("Account position exceeds max exposure limit.", alert.getMessage());
        assertEquals(1200.0, alert.getExposure());
        assertEquals(1000.0, alert.getLimit());
    }

    /**
     * Verifies that the setter methods update all alert fields.
     */
    @Test
    void settersUpdateAlertFields() {
        RiskAlert alert = new RiskAlert("ACC1", "AAPL", "TYPE1", "Message 1", 100.0, 1000.0);

        alert.setAccountId("ACC2");
        alert.setSymbol("MSFT");
        alert.setAlertType("TYPE2");
        alert.setMessage("Message 2");
        alert.setExposure(200.0);
        alert.setLimit(500.0);

        assertEquals("ACC2", alert.getAccountId());
        assertEquals("MSFT", alert.getSymbol());
        assertEquals("TYPE2", alert.getAlertType());
        assertEquals("Message 2", alert.getMessage());
        assertEquals(200.0, alert.getExposure());
        assertEquals(500.0, alert.getLimit());
    }
}
