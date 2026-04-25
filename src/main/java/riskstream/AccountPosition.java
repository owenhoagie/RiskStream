package riskstream;
/**
 * Represents an account's position, including account ID and total exposure.
 */
public class AccountPosition {
    private String accountId;
    private double totalExposure;
    
    /**
     * Constructs a new AccountPosition.
     *
     * @param accountId the account ID
     */
    public AccountPosition(String accountId) {
        this.accountId = accountId;
        this.totalExposure = 0.0;
    }

    /**
     * Gets the account ID.
     *
     * @return the account ID
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the account ID.
     *
     * @param accountId the account ID to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Gets the total exposure.
     *
     * @return the total exposure
     */
    public double getTotalExposure() {
        return totalExposure;
    }

    /**
     * Adds a trade to the account and updates exposure accordingly.
     * 
     * @param trade the TradeEvent associated with this account
     */
    public void addTrade(TradeEvent trade) {
        double exposure = trade.calculateTradeValue();
        totalExposure += exposure;
    }
}
