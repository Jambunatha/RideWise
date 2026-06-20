# SOLID Reflection

## S - Single Responsibility
- `RiderService`, `DriverService`, and `RideService` each focus on one domain concern.
- Strategy classes focus only on one algorithm each.

## O - Open/Closed
- New matching or fare logic can be added by implementing interfaces.
- Core services do not need modification for new strategy classes.

## L - Liskov Substitution
- Any `RideMatchingStrategy` implementation can replace another.
- Any `FareStrategy` implementation can replace another.

## I - Interface Segregation
- Interfaces are small and focused (`RideMatchingStrategy`, `FareStrategy`).
- Callers depend only on methods they use.

## D - Dependency Inversion
- `RideService` depends on strategy abstractions, injected at construction time.
- Runtime behavior can be switched without changing service internals.

## Additional Principles
- Composition over inheritance for entity relationships.
- Low coupling via service boundaries and strategy interfaces.
