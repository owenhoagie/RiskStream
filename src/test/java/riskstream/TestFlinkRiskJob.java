package riskstream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests helper behavior used by the local Flink job.
 */
public class TestFlinkRiskJob {

    /**
     * Verifies that the CSV header is not processed as a trade.
     */
    @Test
    void isDataRowRejectsHeader() {
        assertFalse(FlinkRiskJob.isDataRow("accountId,symbol,quantity,price"));
    }

    /**
     * Verifies that a normal CSV row is processed as trade data.
     */
    @Test
    void isDataRowAcceptsTradeRows() {
        assertTrue(FlinkRiskJob.isDataRow("ACC1,AAPL,10,25.00"));
    }

    /**
     * Verifies that CSV rows are converted into TradeEvent objects.
     */
    @Test
    void parseTradeConvertsCsvRowToTradeEvent() {
        TradeEvent trade = FlinkRiskJob.parseTrade("ACC1,AAPL,10,25.00");

        assertEquals("ACC1", trade.getAccountId());
        assertEquals("AAPL", trade.getSymbol());
        assertEquals(10, trade.getQuantity());
        assertEquals(25.0, trade.getPrice());
    }

    /**
     * Verifies that malformed numeric values become invalid trade events.
     */
    @Test
    void parseTradeCreatesInvalidTradeForBadNumbers() {
        TradeEvent trade = FlinkRiskJob.parseTrade("ACC1,AAPL,bad,25.00");

        assertEquals("ACC1", trade.getAccountId());
        assertEquals("AAPL", trade.getSymbol());
        assertEquals(0, trade.getQuantity());
        assertEquals(0.0, trade.getPrice());
    }

    /**
     * Verifies that alert formatting matches the command-line demo output style.
     */
    @Test
    void formatAlertCreatesReadableOutputLine() {
        RiskAlert alert = new RiskAlert(
            "ACC1",
            "AAPL",
            "EXPOSURE_LIMIT_BREACH",
            "Account position exceeds max exposure limit.",
            1250.0,
            1000.0
        );

        assertEquals(
            "ALERT: EXPOSURE_LIMIT_BREACH account=ACC1 symbol=AAPL exposure=1250.0 limit=1000.0",
            FlinkRiskJob.formatAlert(alert)
        );
    }
}
