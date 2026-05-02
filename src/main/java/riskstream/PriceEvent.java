package riskstream;

import java.io.Serializable;

/**
 * Represents a simple market price update for one symbol.
 */
public class PriceEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String symbol;
    private double price;

    /**
     * Constructs an empty PriceEvent for frameworks that require a no-argument constructor.
     */
    public PriceEvent() {
    }

    /**
     * Constructs a new PriceEvent.
     *
     * @param symbol the symbol associated with the price update
     * @param price the latest price for the symbol
     */
    public PriceEvent(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    /**
     * Gets the symbol associated with the price update.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol associated with the price update.
     *
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the latest price for the symbol.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the latest price for the symbol.
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
