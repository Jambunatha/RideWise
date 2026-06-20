package com.airtribe.ridewise.service;

import com.airtribe.ridewise.enums.VehicleType;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DriverService {
    private Map<String, Driver> drivers;

    public DriverService() {
        this.drivers = new HashMap<>();
    }

    public Driver registerDriver(String name, String currentLocation, VehicleType vehicleType) {
        String driverId = IdGenerator.generateDriverId();
        Driver driver = new Driver(driverId, name, currentLocation, vehicleType);
        drivers.put(driverId, driver);
        return driver;
    }

    public Optional<Driver> getDriverById(String driverId) {
        return Optional.ofNullable(drivers.get(driverId));
    }

    public List<Driver> getAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .collect(Collectors.toList());
    }

    public void updateDriverAvailability(String driverId, boolean available) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }

    public void updateDriverLocation(String driverId, String newLocation) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.setCurrentLocation(newLocation);
        }
    }

    public List<Driver> getAllDrivers() {
        return List.copyOf(drivers.values());
    }
}
