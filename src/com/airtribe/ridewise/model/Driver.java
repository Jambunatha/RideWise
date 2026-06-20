package com.airtribe.ridewise.model;

import com.airtribe.ridewise.enums.VehicleType;

public class Driver {
    private String id;
    private String name;
    private String currentLocation;
    private boolean available;
    private VehicleType vehicleType;
    private int completedRides;

    public Driver(String id, String name, String currentLocation, VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.vehicleType = vehicleType;
        this.available = true;
        this.completedRides = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getCompletedRides() {
        return completedRides;
    }

    public void incrementCompletedRides() {
        this.completedRides++;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", available=" + available +
                ", vehicleType=" + vehicleType +
                ", completedRides=" + completedRides +
                '}';
    }
}
