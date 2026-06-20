package com.airtribe.ridewise.service;

import com.airtribe.ridewise.enums.RideStatus;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RideService {
    private Map<String, Ride> rides;
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;
    private DriverService driverService;

    public RideService(RideMatchingStrategy rideMatchingStrategy, FareStrategy fareStrategy, DriverService driverService) {
        this.rides = new HashMap<>();
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
        this.driverService = driverService;
    }

    public Ride requestRide(Rider rider, double distance) {
        String rideId = IdGenerator.generateRideId();
        Ride ride = new Ride(rideId, rider, distance);
        ride.setStatus(RideStatus.REQUESTED);
        rides.put(rideId, ride);
        return ride;
    }

    public Ride assignDriver(String rideId) throws NoDriverAvailableException {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new NoDriverAvailableException("Ride not found: " + rideId);
        }
        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new NoDriverAvailableException("Ride is not in REQUESTED status: " + rideId);
        }

        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No drivers available for ride: " + rideId);
        }

        Driver selectedDriver = rideMatchingStrategy.findDriver(ride.getRider(), availableDrivers);
        if (selectedDriver == null) {
            throw new NoDriverAvailableException("No suitable driver found for ride: " + rideId);
        }

        ride.setDriver(selectedDriver);
        ride.setStatus(RideStatus.ASSIGNED);
        driverService.updateDriverAvailability(selectedDriver.getId(), false);

        // Calculate and set fare
        double fare = fareStrategy.calculateFare(ride);
        FareReceipt fareReceipt = new FareReceipt(rideId, fare, LocalDateTime.now());
        ride.setFareReceipt(fareReceipt);

        return ride;
    }

    public Ride completeRide(String rideId) throws NoDriverAvailableException {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new NoDriverAvailableException("Ride not found: " + rideId);
        }
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new NoDriverAvailableException("Only ASSIGNED rides can be completed: " + rideId);
        }

        ride.setStatus(RideStatus.COMPLETED);

        // Mark driver as available again
        if (ride.getDriver() != null) {
            driverService.updateDriverAvailability(ride.getDriver().getId(), true);
            ride.getDriver().incrementCompletedRides();
        }

        return ride;
    }

    public void cancelRide(String rideId) throws NoDriverAvailableException {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new NoDriverAvailableException("Ride not found: " + rideId);
        }
        if (ride.getStatus() != RideStatus.REQUESTED && ride.getStatus() != RideStatus.ASSIGNED) {
            throw new NoDriverAvailableException("Only REQUESTED or ASSIGNED rides can be cancelled: " + rideId);
        }

        ride.setStatus(RideStatus.CANCELLED);

        // Mark driver as available if ride was assigned
        if (ride.getDriver() != null) {
            driverService.updateDriverAvailability(ride.getDriver().getId(), true);
        }
    }

    public Optional<Ride> getRideById(String rideId) {
        return Optional.ofNullable(rides.get(rideId));
    }

    public List<Ride> getAllRides() {
        return List.copyOf(rides.values());
    }

    public List<Ride> getRidesByRiderId(String riderId) {
        return rides.values().stream()
                .filter(ride -> ride.getRider().getId().equals(riderId))
                .collect(Collectors.toList());
    }
}
