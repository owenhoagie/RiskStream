package riskstream;

import java.util.Map;
import java.util.HashMap;
/**
 * The RiskEngine class processes trade events, manages account positions, and checks for risk breaches.
 */
public class RiskEngine {
    private int maxExposure;
    private Map<String, AccountPosition> accountPositions;
    
    /**
     * Constructs a new RiskEngine with a specified maximum exposure limit.
     *
     * @param maxExposure the maximum allowed exposure for any account
     */
    public RiskEngine(int maxExposure){
        this.maxExposure = maxExposure;
        accountPositions = new HashMap<>();
    }

    /**
     * Processes a trade event, updates the account position, and returns a RiskAlert if a risk limit is breached or the trade is invalid.
     *
     * @param trade the TradeEvent to process
     * @return a RiskAlert if the trade is invalid or breaches exposure limits, otherwise null
     */
    public RiskAlert processTrade(TradeEvent trade){
        if (!isValidTrade(trade)){
            String accountId = trade != null ? trade.getAccountId() : "UNKNOWN";
            String symbol = trade != null ? trade.getSymbol() : "UNKNOWN";

            return new RiskAlert(
                accountId,
                symbol,
                "INVALID_EVENT",
                "Trade event contains invalid data.",
                0.0,
                maxExposure
            );
        }
        String acc = trade.getAccountId();
        AccountPosition position = null;

        if (accountPositions.containsKey(acc)){
            position = accountPositions.get(acc);
        } else {
            position = new AccountPosition(acc);
            accountPositions.put(acc, position);
        }

        position.addTrade(trade);
        if (position.getTotalExposure() > maxExposure){
            return new RiskAlert(
                acc,
                trade.getSymbol(),
                "EXPOSURE_LIMIT_BREACH",
                "Account position exceeds max exposure limit.",
                position.getTotalExposure(),
                maxExposure
            );
        }
        
        return null;
    }

    /**
     * Validates a TradeEvent for required fields and logical correctness.
     *
     * @param trade the TradeEvent to validate
     * @return true if the trade is valid, false otherwise
     */
    public boolean isValidTrade(TradeEvent trade) {
        return trade != null
            && trade.getAccountId() != null
            && !trade.getAccountId().isBlank()
            && trade.getSymbol() != null
            && !trade.getSymbol().isBlank()
            && trade.getQuantity() != 0
            && trade.getPrice() > 0;
    }

    /**
     * Gets the maximum allowed exposure for any account.
     *
     * @return the maximum exposure
     */
    public int getMaxExposure(){
        return maxExposure;
    }

    /**
     * Gets the map of account IDs to their AccountPosition objects.
     *
     * @return the map of account positions
     */
    public Map<String, AccountPosition> getAccountPositions(){
        return accountPositions;
    }
}
