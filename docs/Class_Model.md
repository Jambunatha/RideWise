# Class Model

## Domain
- `Rider`
    - `id`, `name`, `location`
- `Driver`
    - `id`, `name`, `currentLocation`, `available`, `vehicleType`, `completedRides`
- `Ride`
    - `id`, `rider`, `driver`, `distance`, `status`, `fareReceipt`
- `FareReceipt`
    - `rideId`, `amount`, `generatedAt`
- `RideStatus` enum
    - `REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`
- `VehicleType` enum
    - `BIKE`, `AUTO`, `CAR`

## Strategy Interfaces
- `RideMatchingStrategy`
    - `Driver findDriver(Rider rider, List<Driver> drivers)`
- `FareStrategy`
    - `double calculateFare(Ride ride)`

## Strategy Implementations
- `NearestDriverStrategy`
- `LeastActiveDriverStrategy`
- `DefaultFareStrategy`
- `PeakHourFareStrategy`

## Services
- `RiderService`
    - register and query riders
- `DriverService`
    - register drivers, update availability/location, list available drivers
- `RideService`
    - request, assign, complete, cancel, and query rides

## Utility
- `IdGenerator` for rider/driver/ride IDs

## Exception
- `NoDriverAvailableException`
