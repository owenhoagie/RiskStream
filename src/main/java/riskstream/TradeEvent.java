package riskstream;
/**
 * Represents a trade event, including account, symbol, quantity, and price information.
 */
public class TradeEvent {
    private String accountId;
    private String symbol;
    private int quantity;
    private double price;

    /**
     * Constructs a new TradeEvent.
     * 
     * @param accountId the account ID associated with the trade
     * @param symbol the symbol associated with the trade
     * @param quantity the amount of the symbol purchased
     * @param price the price per symbol
     */
    public TradeEvent(String accountId, String symbol, int quantity, double price) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Gets the accountId of the TradeEvent.
     * 
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the accountId of the TradeEvent.
     *
     * @param accountId the account ID to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Gets the symbol of the TradeEvent.
     * 
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol of the TradeEvent.
     *
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the quantity of the TradeEvent.
     * 
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the TradeEvent.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the price per of the TradeEvent.
     * 
     * @return the price per
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the TradeEvent.
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Calculates the total amount spent on the TradeEvent.
     * 
     * @return the absolute value of the product of quantity and price
     */
    public double calculateTradeValue() {
        return Math.abs(quantity * price);
    }
}
