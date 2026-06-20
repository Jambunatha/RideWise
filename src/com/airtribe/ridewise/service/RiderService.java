package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RiderService {
    private Map<String, Rider> riders;

    public RiderService() {
        this.riders = new HashMap<>();
    }

    public Rider registerRider(String name, String location) {
        String riderId = IdGenerator.generateRiderId();
        Rider rider = new Rider(riderId, name, location);
        riders.put(riderId, rider);
        return rider;
    }

    public Optional<Rider> getRiderById(String riderId) {
        return Optional.ofNullable(riders.get(riderId));
    }

    public void updateRiderLocation(String riderId, String newLocation) {
        Rider rider = riders.get(riderId);
        if (rider != null) {
            rider.setLocation(newLocation);
        }
    }
}
