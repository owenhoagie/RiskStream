# Data

This folder exists to satisfy the assignment repository structure requirement.

The project uses the standard Maven resources layout, so sample input data remains in:

```text
src/main/resources/sample-data/
```

Included datasets:

- `normal-trades.csv` - valid trades that stay under the exposure limit.
- `exposure-breach-trades.csv` - valid trades that cause one exposure-limit alert.
- `invalid-trades.csv` - trades with missing or invalid fields that produce invalid-event alerts.

The data files were not moved into this folder because Maven automatically packages resources from `src/main/resources`.
