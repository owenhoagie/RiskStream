package riskstream;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Local Apache Flink job that reads trade CSV data and prints risk alerts.
 */
public class FlinkRiskJob {
    private static final int DEFAULT_MAX_EXPOSURE = 1000;
    private static final String DEFAULT_INPUT_FILE = "src/main/resources/sample-data/exposure-breach-trades.csv";

    /**
     * Runs the local Flink risk job.
     *
     * @param args optional arguments: max exposure, input CSV path
     * @throws Exception if the Flink job cannot run
     */
    public static void main(String[] args) throws Exception {
        int maxExposure = parseMaxExposure(args);
        String inputFile = parseInputFile(args);

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        FileSource<String> source = FileSource
            .forRecordStreamFormat(new TextLineInputFormat(), new Path(inputFile))
            .build();

        DataStream<TradeEvent> trades = environment
            .fromSource(source, WatermarkStrategy.noWatermarks(), "Trade CSV Source")
            .filter(FlinkRiskJob::isDataRow)
            .map(FlinkRiskJob::parseTrade);

        trades
            .keyBy(FlinkRiskJob::accountKey)
            .flatMap(new AlertFormatter(maxExposure))
            .print();

        environment.execute("RiskStream Local Risk Job");
    }

    static boolean isDataRow(String line) {
        return line != null
            && !line.isBlank()
            && !line.startsWith("accountId");
    }

    static TradeEvent parseTrade(String line) {
        String[] parts = line.split(",", -1);
        String accountId = getCsvValue(parts, 0);
        String symbol = getCsvValue(parts, 1);

        try {
            int quantity = Integer.parseInt(getCsvValue(parts, 2));
            double price = Double.parseDouble(getCsvValue(parts, 3));
            return new TradeEvent(accountId, symbol, quantity, price);
        } catch (NumberFormatException exception) {
            return new TradeEvent(accountId, symbol, 0, 0.0);
        }
    }

    static String accountKey(TradeEvent trade) {
        if (trade == null || trade.getAccountId() == null || trade.getAccountId().isBlank()) {
            return "INVALID_ACCOUNT";
        }

        return trade.getAccountId();
    }

    static String formatAlert(RiskAlert alert) {
        return "ALERT: "
            + alert.getAlertType()
            + " account="
            + alert.getAccountId()
            + " symbol="
            + alert.getSymbol()
            + " exposure="
            + alert.getExposure()
            + " limit="
            + alert.getLimit();
    }

    private static int parseMaxExposure(String[] args) {
        if (args.length == 0) {
            return DEFAULT_MAX_EXPOSURE;
        }

        return Integer.parseInt(args[0]);
    }

    private static String parseInputFile(String[] args) {
        if (args.length < 2) {
            return DEFAULT_INPUT_FILE;
        }

        return args[1];
    }

    private static String getCsvValue(String[] parts, int index) {
        if (index >= parts.length) {
            return "";
        }

        return parts[index].trim();
    }

    /**
     * Converts keyed TradeEvents into printable alert lines.
     */
    private static class AlertFormatter extends RichFlatMapFunction<TradeEvent, String> {
        private static final long serialVersionUID = 1L;

        private final int maxExposure;
        private transient RiskEngine riskEngine;

        AlertFormatter(int maxExposure) {
            this.maxExposure = maxExposure;
        }

        @Override
        public void open(Configuration parameters) {
            riskEngine = new RiskEngine(maxExposure);
        }

        @Override
        public void flatMap(TradeEvent trade, Collector<String> output) {
            RiskAlert alert = riskEngine.processTrade(trade);

            if (alert != null) {
                output.collect(formatAlert(alert));
            }
        }
    }
}
