# Tests

This folder exists to satisfy the assignment repository structure requirement.

The project uses the standard Maven test layout, so automated tests remain in:

```text
src/test/java/riskstream/
```

Test coverage includes:

- model class behavior for `TradeEvent`, `PriceEvent`, `AccountPosition`, and `RiskAlert`
- risk-engine validation and exposure calculations
- exposure-limit alert behavior
- invalid-event alert behavior
- helper behavior used by the local Flink job

Run all tests from the repository root:

```bash
mvn clean test
```
