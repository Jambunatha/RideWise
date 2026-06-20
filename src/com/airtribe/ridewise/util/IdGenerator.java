package com.airtribe.ridewise.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong riderId = new AtomicLong(1000);
    private static final AtomicLong driverId = new AtomicLong(2000);
    private static final AtomicLong rideId = new AtomicLong(3000);

    public static String generateRiderId() {
        return "R" + riderId.getAndIncrement();
    }

    public static String generateDriverId() {
        return "D" + driverId.getAndIncrement();
    }

    public static String generateRideId() {
        return "RD" + rideId.getAndIncrement();
    }
}
