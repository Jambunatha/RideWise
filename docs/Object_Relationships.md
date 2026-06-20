# Object Relationships

## Core Relationships
- `Rider` <-> `Ride` : association
    - One rider can have many rides over time.
- `Driver` <-> `Ride` : association
    - One driver can serve many rides over time, one active assignment at a time.
- `Ride` -> `FareReceipt` : composition
    - Fare receipt is generated for a specific ride assignment.

## Service Collaborations
- `Main` uses service layer only.
- `RideService` collaborates with:
    - `DriverService` for availability updates and candidate drivers
    - `RideMatchingStrategy` for selecting a driver
    - `FareStrategy` for pricing

## Lifecycle Constraints
- `REQUESTED` -> `ASSIGNED`
- `ASSIGNED` -> `COMPLETED`
- `REQUESTED` or `ASSIGNED` -> `CANCELLED`

These transitions are validated in `RideService` to prevent invalid state changes.
