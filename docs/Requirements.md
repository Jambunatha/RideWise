# RideWise Requirements

## Functional Requirements
- Register riders.
- Register drivers.
- Show available drivers.
- Request a ride.
- Match a driver using a matching strategy.
- Calculate fare using a fare strategy.
- Track ride lifecycle states:
    - `REQUESTED`
    - `ASSIGNED`
    - `COMPLETED`
    - `CANCELLED`

## Non-Functional Requirements
- Pricing logic should be easy to extend.
- Driver matching logic should be easy to replace.
- Services should remain loosely coupled.
- Code should be readable and maintainable.

## Console Menu Requirements
- Add Rider
- Add Driver
- View Available Drivers
- Request Ride
- Complete Ride
- View Rides
- Exit

## Notes
- Service layer must be used from the UI.
- Input validation must handle invalid values safely.
- Strategy implementations should be swappable through constructor injection.
