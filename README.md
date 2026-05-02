# RiskStream QA Lab

RiskStream QA Lab is a small Java/Maven project that processes synthetic trade events, tracks simplified account exposure, and emits risk alerts when trades are invalid or when an account exceeds an exposure limit.

The project was built as a beginner-friendly Apache Flink and Java practice project. It is not a real trading, clearing, or risk-management system. The finance model is intentionally simplified so the focus stays on Java classes, Maven, Flink stream processing, JUnit tests, reproducible sample data, and clear documentation.

## Technologies Used

- Java 11+
- Maven
- Apache Flink
- JUnit 5
- CSV sample data

## Author And Contribution Summary

Author: Owen Hoag

This is an individual programming project. I designed the project scope, created the Java/Maven structure, implemented the event models and risk logic, added Apache Flink as a local streaming pipeline, wrote JUnit tests, created sample trade datasets, debugged the build/run commands, and documented how to reproduce the results.

## Video Demonstration

YouTube demo link: 

## What The Project Does

- Reads synthetic trade records with account ID, symbol, quantity, and price.
- Rejects invalid trades with missing account IDs, missing symbols, zero quantity, or non-positive price.
- Calculates simplified exposure as `abs(quantity * price)`.
- Tracks total exposure by account.
- Emits an `EXPOSURE_LIMIT_BREACH` alert when an account exceeds the configured max exposure.
- Emits an `INVALID_EVENT` alert when input data is incomplete or invalid.

## Project Structure

This repository keeps the normal Maven layout so the project can build and run without custom Maven configuration. The assignment-required top-level folders are included as navigation folders that explain where each required artifact lives.

```text
code/
  README.md - explains where the Maven source code lives

data/
  README.md - explains where sample CSV input data lives

tests/
  README.md - explains where JUnit test files live

docs/
  README.md - supporting project documentation folder

report/
  README.md - reserved for the final report document

src/main/java/riskstream/
  AccountPosition.java
  FlinkRiskJob.java
  Main.java
  PriceEvent.java
  RiskAlert.java
  RiskEngine.java
  TradeEvent.java

src/main/resources/sample-data/
  normal-trades.csv
  exposure-breach-trades.csv
  invalid-trades.csv

src/test/java/riskstream/
  JUnit tests for the model classes, risk engine, and Flink helpers
```

## Build And Test

If multiple JDKs are installed, Java 11 is the safest runtime for this local Flink version.

On macOS:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
export PATH="$JAVA_HOME/bin:$PATH"
```

Run the tests:

```bash
mvn clean test
```

Expected result:

```text
Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Run The Flink Job

First package the project and copy runtime dependencies:

```bash
mvn clean package dependency:copy-dependencies
```

Run the Flink job against the exposure-breach sample:

```bash
java --add-opens=java.base/java.util=ALL-UNNAMED \
     --add-opens=java.base/java.nio=ALL-UNNAMED \
     --add-opens=java.base/java.lang=ALL-UNNAMED \
     -cp "target/classes:target/dependency/*" \
     riskstream.FlinkRiskJob 1000 src/main/resources/sample-data/exposure-breach-trades.csv
```

Expected alert output:

```text
ALERT: EXPOSURE_LIMIT_BREACH account=ACC1 symbol=GOOG exposure=1350.0 limit=1000.0
```

Run the Flink job against the invalid-data sample:

```bash
java --add-opens=java.base/java.util=ALL-UNNAMED \
     --add-opens=java.base/java.nio=ALL-UNNAMED \
     --add-opens=java.base/java.lang=ALL-UNNAMED \
     -cp "target/classes:target/dependency/*" \
     riskstream.FlinkRiskJob 1000 src/main/resources/sample-data/invalid-trades.csv
```

Expected alert output:

```text
ALERT: INVALID_EVENT account= symbol=AAPL exposure=0.0 limit=1000.0
ALERT: INVALID_EVENT account=ACC1 symbol= exposure=0.0 limit=1000.0
ALERT: INVALID_EVENT account=ACC2 symbol=GOOG exposure=0.0 limit=1000.0
ALERT: INVALID_EVENT account=ACC3 symbol=TSLA exposure=0.0 limit=1000.0
```

## Optional Plain Java Demo

The original non-Flink demo still works and processes all sample CSV files:

```bash
mvn clean package
java -cp target/classes riskstream.Main
```

## Sample Input

From `exposure-breach-trades.csv`:

```csv
accountId,symbol,quantity,price
ACC1,AAPL,10,25.00
ACC1,MSFT,15,40.00
ACC1,GOOG,5,100.00
ACC2,TSLA,2,75.00
```

With a limit of `1000`, account `ACC1` reaches `1350.0` exposure after the GOOG trade and produces an alert.

## Flink Pipeline

```text
CSV file
  -> FileSource with TextLineInputFormat
  -> filter header/blank rows
  -> map CSV rows to TradeEvent objects
  -> keyBy account ID
  -> RiskEngine processes each trade
  -> print RiskAlert output
```

## Known Limitations

- The exposure formula is simplified and does not represent real financial risk calculations.
- The Flink job runs locally using a small sample CSV file, not Kafka or a production stream.
- `PriceEvent` exists as a basic event model, but the current pipeline focuses on trade events.
- There is no database, dashboard, API, Docker setup, or deployment configuration.

## Future Improvements

- Add JSON output for alerts.
- Add configurable risk limits from a properties file.
- Add a price-shock rule using `PriceEvent`.
- Add Kafka as a streaming source after the local version is stable.
- Add more edge-case test data.
