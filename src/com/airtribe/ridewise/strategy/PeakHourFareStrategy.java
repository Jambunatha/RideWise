package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 50.0; // Base fare
    private static final double PER_KM_CHARGE = 10.0; // Standard per-km charge
    private static final double PEAK_HOUR_MULTIPLIER = 1.5; // 50% surge during peak hours
    private static final LocalTime PEAK_START = LocalTime.of(8, 0); // 8 AM
    private static final LocalTime PEAK_END = LocalTime.of(10, 0); // 10 AM or evening peak

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = BASE_FARE + (ride.getDistance() * PER_KM_CHARGE);
        LocalTime now = LocalDateTime.now().toLocalTime();
        if (isPeakHour(now)) {
            baseFare *= PEAK_HOUR_MULTIPLIER;
        }

        return baseFare;
    }

    private boolean isPeakHour(LocalTime time) {
        // Simplified peak hour logic - consider 8-10 AM or 6-8 PM as peak
        return (time.isAfter(PEAK_START) && time.isBefore(PEAK_END)) ||
                (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(20, 0)));
    }
}
