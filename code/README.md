# Code

This folder exists to satisfy the assignment repository structure requirement.

The project uses the standard Maven Java layout, so the actual source code remains in:

```text
src/main/java/riskstream/
```

Main source files:

- `FlinkRiskJob.java` - local Apache Flink pipeline that reads trade CSV data and prints risk alerts.
- `Main.java` - plain Java demo runner that processes the sample CSV files without Flink.
- `RiskEngine.java` - core risk logic for invalid trade detection and exposure-limit alerts.
- `TradeEvent.java` - trade event model.
- `PriceEvent.java` - simple price event model for future price-based rules.
- `AccountPosition.java` - account exposure tracker.
- `RiskAlert.java` - alert model.

The code was not moved into this folder because Maven expects Java source files under `src/main/java` by default.
