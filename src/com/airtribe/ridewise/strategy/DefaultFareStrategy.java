package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 50.0; // Base fare in currency units
    private static final double PER_KM_CHARGE = 10.0; // Charge per km

    @Override
    public double calculateFare(Ride ride) {
        // Fare = Base + (Distance * Per-km charge)
        return BASE_FARE + (ride.getDistance() * PER_KM_CHARGE);
    }
}
