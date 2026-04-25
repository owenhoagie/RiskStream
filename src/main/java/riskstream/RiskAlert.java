package riskstream;
/**
 * Represents a risk alert, including alert type, message, and exposure details for an account and symbol.
 */
public class RiskAlert {
    private String accountId;
    private String symbol;
    private String alertType;
    private String message;
    private double exposure;
    private double limit;

    /**
     * Constructs a new RiskAlert.
     *
     * @param accountId the account ID associated with the alert
     * @param symbol the symbol associated with the alert
     * @param alertType the type of alert
     * @param message the alert message
     * @param exposure the exposure value
     * @param limit the limit value
     */
    public RiskAlert(String accountId, String symbol, String alertType, String message, double exposure, double limit) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.alertType = alertType;
        this.message = message;
        this.exposure = exposure;
        this.limit = limit;
    }

    /**
     * Gets the account ID associated with the alert.
     *
     * @return the account ID
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the account ID associated with the alert.
     *
     * @param accountId the account ID to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Gets the symbol associated with the alert.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol associated with the alert.
     *
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the type of alert.
     *
     * @return the alert type
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * Sets the type of alert.
     *
     * @param alertType the alert type to set
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * Gets the alert message.
     *
     * @return the alert message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the alert message.
     *
     * @param message the alert message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the exposure value.
     *
     * @return the exposure value
     */
    public double getExposure() {
        return exposure;
    }

    /**
     * Sets the exposure value.
     *
     * @param exposure the exposure value to set
     */
    public void setExposure(double exposure) {
        this.exposure = exposure;
    }

    /**
     * Gets the limit value.
     *
     * @return the limit value
     */
    public double getLimit() {
        return limit;
    }

    /**
     * Sets the limit value.
     *
     * @param limit the limit value to set
     */
    public void setLimit(double limit) {
        this.limit = limit;
    }
}
