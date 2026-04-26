package riskstream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Main controller for the Risk System. 
 * Takes in a input as maxExposure, otherwise defaults to 1000.
 */
public class Main {
    private static final List<String> SAMPLE_DATA_FILES = List.of(
        "sample-data/normal-trades.csv",
        "sample-data/exposure-breach-trades.csv",
        "sample-data/invalid-trades.csv"
    );

    private RiskEngine engine;
    
    /**
     * Constructs a new system to handle TradeEvents.
     * 
     * @param maxExposure the maximum exposure for the RiskEngine  
     */
    private Main(int maxExposure){
        this.engine = new RiskEngine(maxExposure);
    }

    /**
     * Gets the RiskEngine used for this system.
     *
     * @return the RiskEngine
     */
    public RiskEngine getEngine(){
        return engine;
    }

    /**
     * Main controller for the Risk System
     * 
     * @param args first arg is the maxExposure value for the RiskEngine
     * @throws IOException if a sample data file cannot be read
     */
    public static void main(String[] args) throws IOException {
        int exposure = 1000;
        if (args.length > 0){
            exposure = Integer.parseInt(args[0]);
        }

        for (String fileName : SAMPLE_DATA_FILES) {
            Main controller = new Main(exposure);
            controller.processSampleFile(fileName);
        }
    }

    /**
     * Reads a sample CSV file from resources and processes each trade in it.
     *
     * @param fileName the resource file name to process
     * @throws IOException if the resource cannot be read
     */
    private void processSampleFile(String fileName) throws IOException {
        System.out.println("Processing " + fileName);

        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("Could not find sample data file: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                TradeEvent trade = parseTrade(line);
                RiskAlert alert = engine.processTrade(trade);
                printResult(trade, alert);
            }
        }

        System.out.println();
    }

    /**
     * Converts a CSV row into a TradeEvent.
     *
     * @param line the CSV row to parse
     * @return the TradeEvent represented by the row
     */
    private TradeEvent parseTrade(String line) {
        String[] parts = line.split(",", -1);
        String accountId = parts[0];
        String symbol = parts[1];
        int quantity = Integer.parseInt(parts[2]);
        double price = Double.parseDouble(parts[3]);

        return new TradeEvent(accountId, symbol, quantity, price);
    }

    /**
     * Prints either a successful trade result or the alert produced by the risk engine.
     *
     * @param trade the processed trade
     * @param alert the alert returned by the risk engine, or null when no alert occurred
     */
    private void printResult(TradeEvent trade, RiskAlert alert) {
        if (alert == null) {
            System.out.println("OK: " + trade.getAccountId() + " " + trade.getSymbol());
            return;
        }

        System.out.println(
            "ALERT: "
                + alert.getAlertType()
                + " account="
                + alert.getAccountId()
                + " symbol="
                + alert.getSymbol()
                + " exposure="
                + alert.getExposure()
                + " limit="
                + alert.getLimit()
        );
    }
}
